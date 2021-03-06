package lasermod.proxy;

import lasermod.client.gui.GuiAdvancedLaser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ProPercivalalb
 */
public class CommonProxy implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if (ID == GuiAdvancedLaser.GUI_ID) { return new GuiAdvancedLaser(); } 
		return null;
	}
	
	public void registerHandlers() {}

	public void onPreLoad() {}

	public int armorRender(String str) { return 0; }

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
}
