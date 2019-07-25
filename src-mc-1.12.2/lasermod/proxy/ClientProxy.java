package lasermod.proxy;

import lasermod.client.render.block.TileEntityAdvancedLaserRenderer;
import lasermod.client.render.block.TileEntityBasicLaserRenderer;
import lasermod.client.render.block.TileEntityColourConverterRenderer;
import lasermod.client.render.block.TileEntityLaserFilterRenderer;
import lasermod.client.render.block.TileEntityLuminousLampRenderer;
import lasermod.client.render.block.TileEntityReflectorRenderer;
import lasermod.client.render.block.TileEntitySmallColourConverterRenderer;
import lasermod.handler.ScreenRenderHandler;
import lasermod.handler.WorldOverlayHandler;
import lasermod.tileentity.TileEntityAdvancedLaser;
import lasermod.tileentity.TileEntityBasicLaser;
import lasermod.tileentity.TileEntityColourConverter;
import lasermod.tileentity.TileEntityLaserFilter;
import lasermod.tileentity.TileEntityLuminousLamp;
import lasermod.tileentity.TileEntityReflector;
import lasermod.tileentity.TileEntitySmallColourConverter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ProPercivalalb
 */
public class ClientProxy extends CommonProxy {
	
	public ClientProxy() {
		super();
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
	}
	
	@Override
	public void preInit() { // clientSetup
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasicLaser.class, new TileEntityBasicLaserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvancedLaser.class, new TileEntityAdvancedLaserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColourConverter.class, new TileEntityColourConverterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReflector.class, new TileEntityReflectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuminousLamp.class, new TileEntityLuminousLampRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySmallColourConverter.class, new TileEntitySmallColourConverterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserFilter.class, new TileEntityLaserFilterRenderer());
	}
	
	@Override
	public void registerHandlers() {
		MinecraftForge.EVENT_BUS.register(new ScreenRenderHandler());
		MinecraftForge.EVENT_BUS.register(new WorldOverlayHandler());
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
	
}
