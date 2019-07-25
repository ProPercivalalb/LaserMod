package lasermod.tileentity;

import java.util.ArrayList;

import lasermod.LaserMod;
import lasermod.api.LaserInGame;
import lasermod.api.LaserType;
import lasermod.api.base.TileEntityMultiSidedReceiver;
import lasermod.block.BlockLaserDetector;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.LaserDetectorMessage;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * @author ProPercivalalb
 */

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileEntityLaserDetector extends TileEntityMultiSidedReceiver implements SimpleComponent {
	
	@Override
	public void sendUpdateDescription() {
		PacketDispatcher.sendToAllTracking(new LaserDetectorMessage(this), this);
	}

	@Override
	public void onLaserPass(World world) {
		world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLaserDetector.ACTIVE, true));
	}

	@Override
	public void onLaserRemoved(World world) {
		if(this.lasers.isEmpty())
			world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLaserDetector.ACTIVE, false));
	}
	
	//TODO
		/**
	@Override
	public Packet getDescriptionPacket() {
	    return PacketDispatcher.getPacket(new LaserDetectorMessage(this));
	}**/
	
	
	
	// OpenComputers interaction

	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		// TODO Auto-generated method stub
		return "laser_detector";
	}

	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isActive(Context context, Arguments args) {
		return new Object[] {!this.noLaserInputs()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLaserAtSide(Context context, Arguments args) {
		if(this.noLaserInputs()) {
			return new Object[] {null};
		//must be at least one
		}
		int dex = args.checkInteger(0);
		return getLaserHelper(dex);
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLasers(Context context, Arguments args) {
		Object[] obj = new Object[6];
		for(int i = 0; i < 6; i++) {
			obj[i] = getLaserHelper(i);
		}
		return obj;
	}
	
	/**This was used in multiple classes so I put it in a separate method*/
	private Object[] getLaserHelper(int dex) {
		LaserInGame laser = this.getLaserFromSide(EnumFacing.byIndex(dex));
		if(laser == null) { //not at given index
			return new Object[] {null};
		}
		ArrayList<LaserType> type = laser.getLaserType();
		Object[] strings = new Object[type.size()];
		for(int i = 0; i < type.size(); i++) {
			strings[i] = type.get(i).getTranslationKey();
		}
		return new Object[] {laser.red, laser.green, laser.blue, laser.getDirection().getIndex(), strings};
	}
}