package lasermod.tileentity;

import java.util.Arrays;
import java.util.List;

import lasermod.LaserMod;
import lasermod.ModBlocks;
import lasermod.ModLasers;
import lasermod.api.ILaserProvider;
import lasermod.api.ILaserReceiver;
import lasermod.api.LaserInGame;
import lasermod.api.base.TileEntitySingleSidedReceiver;
import lasermod.block.BlockPoweredLaser;
import lasermod.block.BlockPoweredRedstone;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.LaserFilterMessage;
import lasermod.util.BlockActionPos;
import lasermod.util.LaserUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityLaserFilter extends TileEntitySingleSidedReceiver implements ILaserProvider {

	public EnumDyeColor colour = EnumDyeColor.RED;//TODO makes most sense with current filtering
	
	@Override
	public void sendUpdateDescription() {
		PacketDispatcher.sendToAllTracking(new LaserFilterMessage(this), this);
	}

	@Override
	public void onLaserPass(World world) {
		world.scheduleUpdate(this.pos, ModBlocks.LASER_FILTER, 4);
	}

	@Override
	public void onLaserRemoved(World world) {
		world.scheduleUpdate(this.pos, ModBlocks.LASER_FILTER, 4);
	}

	@Override
	public EnumFacing getInputSide() {
		IBlockState state = this.getWorld().getBlockState(this.pos);
		
		return state.getValue(BlockPoweredRedstone.FACING).getOpposite();
	}

	@Override
	public LaserInGame getOutputLaser(EnumFacing side) {
		if(this.laser != null) {
			LaserInGame outputLaser = this.laser.copy();
			//TODO temporary function, get proper filtering
			if (this.passesThroughFilter()) {
			    return outputLaser;
			}
		}
		return null;
	}
	
	public boolean passesThroughFilter() {
	    if(this.laser != null) {
            //TODO temporary function, get proper filtering
            if(this.laser.getLaserType().contains(ModLasers.FIRE)) {
                return true;
            } else {
                return false;
            }
	    }
        
        return true;
	}

	@Override
	public int getRange(EnumFacing dir) {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isForgeMultipart() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmittingFromSide(World worldIn, BlockPos askerPos, EnumFacing side) {
		return this.laser != null && side == this.getInputSide().getOpposite() && this.passesThroughFilter();
	}

	@Override
	public List<LaserInGame> getOutputLasers() {
		IBlockState state = this.getWorld().getBlockState(this.pos);
		
		return Arrays.asList(this.getOutputLaser(state.getValue(BlockPoweredRedstone.FACING)));
	}

	@Override
	public void tickLaserLogic() {
		super.tickLaserLogic();
		
		EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockPoweredRedstone.FACING);
		
		BlockActionPos bap = LaserUtil.getFirstBlock(this, facing);
		if(bap == null) {}
		else if(bap.isLaserReceiver(facing)) {
			ILaserReceiver reciver = bap.getLaserReceiver(facing);
	    	LaserInGame laserInGame = this.getOutputLaser(facing);
	    	
	    	if(reciver.canReceive(this.world, this.pos, facing.getOpposite(), laserInGame))
	    		reciver.onLaserIncident(this.world, this.pos, facing.getOpposite(), laserInGame);
	    }
	    else {
	        LaserInGame lig = this.getOutputLaser(facing);
	        if (lig != null) {
	            lig.getLaserType().stream().forEach(laser -> laser.actionOnBlock(bap));
	        }
	    }
	}
	

	@Override
	public void tickLaserAction(boolean client) {
		if(this.laser != null) {
			IBlockState state = this.getWorld().getBlockState(this.pos);
			
			LaserUtil.performLaserAction(this, state.getValue(BlockPoweredLaser.FACING), this.pos);
		}
	}
	
	public TileEntityLaserFilter setColour(EnumDyeColor colour) {
		this.colour = colour;
		return this;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(tag.hasKey("colour"))
			this.colour = EnumDyeColor.byMetadata(tag.getInteger("colour"));
		if(tag.hasKey("laser"))
			this.laser = LaserInGame.readFromNBT(tag.getCompoundTag("laser"));
		else 
			this.laser = null;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("colour", this.colour.getMetadata());
		if(this.laser != null)
			tag.setTag("laser", this.laser.writeToNBT(new NBTTagCompound()));
		
		return tag;
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
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
    }
}
