package lasermod.client.render.block;

import lasermod.api.LaserCollisionBoxes;
import lasermod.api.LaserInGame;
import lasermod.api.LaserToRender;
import lasermod.block.BlockBasicLaser;
import lasermod.helper.ClientHelper;
import lasermod.tileentity.TileEntityLaserFilter;
import lasermod.util.LaserUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityLaserFilterRenderer extends TileEntitySpecialRenderer<TileEntityLaserFilter> {

	@Override
    public void render(TileEntityLaserFilter te, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		if(te.hasWorld() && te.getWorld().getBlockState(te.getPos()).getValue(BlockBasicLaser.POWERED)) {
			EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockBasicLaser.FACING);
			
	    	LaserInGame laserInGame = te.getOutputLaser(facing.getOpposite());
	    	if(laserInGame == null) return;
	    	
	    	float alpha = laserInGame.shouldRenderLaser(ClientHelper.getPlayer());

	    	if(alpha == 0.0F) return;
	
	        AxisAlignedBB laserOutline = LaserUtil.getLaserOutline(te, facing, x, y, z);
	        LaserCollisionBoxes.addLaserCollision(new LaserToRender(laserInGame, laserOutline, x, y, z, te.getPos(), facing, alpha, true));
		}

    }
	
	@Override
	public boolean isGlobalRenderer(TileEntityLaserFilter te) {
        return true;
    }
}
