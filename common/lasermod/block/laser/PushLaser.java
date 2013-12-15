package lasermod.block.laser;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.player.EntityPlayer;
import lasermod.api.ILaser;

/**
 * @author ProPercivalalb
 */
public class PushLaser implements ILaser {

	public static final float SPEED_MULTIPLYER = 1.0F;
	
	@Override
	public void performActionOnEntitiesServer(List<Entity> entities, int direction) {
		for(Entity entity : entities) {
			
		}
	}
	
	@Override
	public void performActionOnEntitiesClient(List<Entity> entities, int direction) {
		for(Entity entity : entities) {
			
		}
	}
	
	@Override
	public void performActionOnEntitiesBoth(List<Entity> entities, int direction) {
		for(Entity entity : entities) {
			double verticalSpeed = 0.050000000000000003D;
            double horizonalSpeed = 0.29999999999999999D;
            verticalSpeed *= SPEED_MULTIPLYER;

            if(entity instanceof EntityItem)
                horizonalSpeed *= 2.34D;

            if(!(entity instanceof EntityItem))
                verticalSpeed = 0.0D;

            if (entity instanceof EntityMinecart)
                verticalSpeed *= 0.5D;

            if ((entity instanceof EntityFallingSand) && direction == 1)
                verticalSpeed = 0.0D;

            if (direction == 0 && entity.motionY > -horizonalSpeed)
                entity.motionY += -verticalSpeed;

            if (direction == 1 && entity.motionY < horizonalSpeed * 0.5D)
                entity.motionY += verticalSpeed;

            if (direction == 2 && entity.motionZ > -horizonalSpeed)
                entity.motionZ += -verticalSpeed;

            if (direction == 3 && entity.motionZ < horizonalSpeed)
                entity.motionZ += verticalSpeed;

            if (direction == 4 && entity.motionX > -horizonalSpeed)
                entity.motionX += -verticalSpeed;

            if (direction == 5 && entity.motionX < horizonalSpeed)
                entity.motionX += verticalSpeed;
		}
	}
	
	@Override
	public boolean shouldRenderLaser(EntityPlayer player, int direction) {
		return true;
	}
}
