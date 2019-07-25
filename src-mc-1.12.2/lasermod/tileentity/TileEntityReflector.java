package lasermod.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lasermod.ModBlocks;
import lasermod.api.ILaserProvider;
import lasermod.api.ILaserReceiver;
import lasermod.api.LaserInGame;
import lasermod.api.base.TileEntityMultiSidedReceiver;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.ReflectorMessage;
import lasermod.util.BlockActionPos;
import lasermod.util.LaserUtil;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * @author ProPercivalalb
 */

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileEntityReflector extends TileEntityMultiSidedReceiver implements ILaserProvider, SimpleComponent {

	public boolean[] sideClosed = new boolean[] { true, true, true, true, true, true };

	@Override
	public void tickLaserLogic() {
		super.tickLaserLogic();

		for (EnumFacing facing : EnumFacing.VALUES) {
			if (this.sideClosed[facing.ordinal()] || this.containsInputSide(facing) || this.noLaserInputs())
				continue;

			BlockActionPos bap = LaserUtil.getFirstBlock(this, facing);
			if (bap == null) {
			} else if (bap.isLaserReceiver(facing)) {
				ILaserReceiver reciver = bap.getLaserReceiver(facing);
				LaserInGame laserInGame = this.getOutputLaser(facing);

				if (reciver.canReceive(this.world, this.pos, facing.getOpposite(), laserInGame))
					reciver.onLaserIncident(this.world, this.pos, facing.getOpposite(), laserInGame);
			} else {
				this.getOutputLaser(facing).getLaserType().stream().forEach(laser -> laser.actionOnBlock(bap));
			}
		}
	}

	@Override
	public boolean checkPowerOnSide(EnumFacing dir) {
		return !this.sideClosed[dir.ordinal()] && this.containsInputSide(dir);
	}

	@Override
	public void tickLaserAction(boolean client) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (this.sideClosed[dir.ordinal()] || this.containsInputSide(dir) || this.noLaserInputs())
				continue;
			LaserUtil.performLaserAction(this, dir, this.pos);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		int[] close = tag.getIntArray("closeSides");
		for (int i = 0; i < close.length; ++i)
			this.sideClosed[i] = close[i] == 1;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		int[] close = new int[this.sideClosed.length];
		for (int i = 0; i < this.sideClosed.length; ++i)
			close[i] = this.sideClosed[i] ? 1 : 0;
		tag.setIntArray("closeSides", close);

		return tag;
	}

	// TODO
	/**
	 * @Override public Packet getDescriptionPacket() { return
	 *           PacketDispatcher.getPacket(new ReflectorMessage(this)); }
	 **/

	public int openSides() {
		int count = 0;
		for (boolean bool : this.sideClosed)
			if (!bool)
				count++;
		return count;
	}

	@Override
	public LaserInGame getOutputLaser(EnumFacing dir) {
		return this.getCombinedOutputLaser(dir);
	}

	@Override
	public boolean canReceive(World world, BlockPos orginPos, EnumFacing dir, LaserInGame laserInGame) {
		return !this.sideClosed[dir.ordinal()]
				&& (!this.containsInputSide(dir) || super.canReceive(world, orginPos, dir, laserInGame));
	}

	@Override
	public boolean isEmittingFromSide(World world, BlockPos askerPos, EnumFacing dir) {
		return !this.sideClosed[dir.ordinal()] && !this.noLaserInputs() && !this.containsInputSide(dir);
	}

	@Override
	public int getRange(EnumFacing dir) {
		int total = 0;

		for (EnumFacing dir2 : EnumFacing.VALUES)
			if (!this.sideClosed[dir2.ordinal()] && !this.containsInputSide(dir2))
				total += 1;
		if (total == 0)
			total = 1;
		return MathHelper.floor(64D / total);
	}

	@Override
	public boolean isForgeMultipart() {
		return false;
	}

	@Override
	public void sendUpdateDescription() {
		PacketDispatcher.sendToAllTracking(new ReflectorMessage(this), this);
	}

	@Override
	public void onLaserPass(World world) {
		world.scheduleUpdate(this.pos, ModBlocks.REFLECTOR, 4);
	}

	@Override
	public void onLaserRemoved(World world) {
		world.scheduleUpdate(this.pos, ModBlocks.REFLECTOR, 4);
	}

	@Override
	public List<LaserInGame> getOutputLasers() {
		List<LaserInGame> list = new ArrayList<LaserInGame>();
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (this.sideClosed[dir.ordinal()] || this.containsInputSide(dir) || this.noLaserInputs())
				continue;
			list.add(this.getOutputLaser(dir));
		}

		return list;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	// OpenComputers interaction

	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		// TODO Auto-generated method stub
		return "laser_reflector";
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getSideStates(Context con, Arguments args) {
		Object[] objs = new Object[6];
		for (int i = 0; i < 6; i++) {
			objs[i] = !this.sideClosed[i];
		}
		return objs;
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getSideState(Context con, Arguments args) {
		return new Object[] { !this.sideClosed[args.checkInteger(0)] };
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] setSideState(Context con, Arguments args) {
		boolean flag = args.checkBoolean(1);
		int dex = args.checkInteger(0);
		boolean flag2 = !this.sideClosed[dex];
		this.sideClosed[dex] = !flag;
		world.scheduleUpdate(pos, this.getBlockType(), 4);
		this.validate();
		world.markAndNotifyBlock(pos, world.getChunk(pos), world.getBlockState(pos),
				world.getBlockState(pos), 2);
		return new Object[] { flag ^ flag2 };// returns whether or not the side changed
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] setSideStates(Context con, Arguments args) {
		boolean[] temp = Arrays.copyOf(sideClosed, 6);
		for (int i = 0; i < 6; i++) {
			this.sideClosed[i] = !args.checkBoolean(i);
		}
		world.scheduleUpdate(pos, this.getBlockType(), 4);
		this.validate();
		world.markAndNotifyBlock(pos, world.getChunk(pos), world.getBlockState(pos),
				world.getBlockState(pos), 2);
		return new Object[] { temp }; // returns previous state
	}
}