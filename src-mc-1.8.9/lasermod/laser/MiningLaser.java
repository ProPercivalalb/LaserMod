package lasermod.laser;

import java.util.List;

import lasermod.api.ILaser;
import lasermod.api.LaserModAPI;
import lasermod.util.BlockActionPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLLog;

/**
 * @author ProPercivalalb
 */
public class MiningLaser implements ILaser {

	@Override
	public void performActionOnEntitiesServer(List<Entity> entities, EnumFacing dir) {
		
	}
	
	@Override
	public void performActionOnEntitiesClient(List<Entity> entities, EnumFacing dir) {
		
	}
	
	@Override
	public void performActionOnEntitiesBoth(List<Entity> entities, EnumFacing dir) {
		
	}
	
	@Override
	public boolean shouldRenderLaser(EntityPlayer player, EnumFacing dir) {
		return true;
	}
	
	@Override
	public void actionOnBlock(BlockActionPos action) {
		if(LaserModAPI.MINING_BLACKLIST.contains(action.block, action.meta)) return;
		
		int harvestLevel = action.block.getHarvestLevel(action.state);
		FMLLog.info("harvest " + harvestLevel);
		action.block.dropBlockAsItem(action.world, action.pos, action.state, 0);
		action.world.setBlockToAir(action.pos);
		action.world.playSoundEffect(action.pos.getX() + 0.5D, action.pos.getY() + 0.5D, action.pos.getZ() + 0.5D, "random.fizz", 0.3F, action.world.rand.nextFloat() * 0.4F + 0.2F);
	}
}
