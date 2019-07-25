package lasermod;

import lasermod.block.BlockAdvancedLaser;
import lasermod.block.BlockBasicLaser;
import lasermod.block.BlockColourConverter;
import lasermod.block.BlockLaserDetector;
import lasermod.block.BlockLaserFilter;
import lasermod.block.BlockLensWorkbench;
import lasermod.block.BlockLuminousLamp;
import lasermod.block.BlockReflector;
import lasermod.block.BlockSmallColourConverter;
import lasermod.lib.BlockNames;
import lasermod.lib.Reference;
import lasermod.tileentity.TileEntityAdvancedLaser;
import lasermod.tileentity.TileEntityBasicLaser;
import lasermod.tileentity.TileEntityColourConverter;
import lasermod.tileentity.TileEntityLaserDetector;
import lasermod.tileentity.TileEntityLaserFilter;
import lasermod.tileentity.TileEntityLuminousLamp;
import lasermod.tileentity.TileEntityReflector;
import lasermod.tileentity.TileEntitySmallColourConverter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author ProPercivalalb
 */
public class ModBlocks {
	
	@ObjectHolder(BlockNames.LASER_BASIC)
	public static Block LASER_BASIC;
	@ObjectHolder(BlockNames.LASER_ADVANCED)
	public static Block LASER_ADVANCED;
	@ObjectHolder(BlockNames.LASER_DETECTOR)
	public static Block LASER_DETECTOR;
	@ObjectHolder(BlockNames.REFLECTOR)
	public static Block REFLECTOR;
	@ObjectHolder(BlockNames.COLOUR_CONVERTER)
	public static Block COLOUR_CONVERTER;
	@ObjectHolder(BlockNames.COLOUR_CONVERTER_SMALL)
	public static Block COLOUR_CONVERTER_SMALL;
	@ObjectHolder(BlockNames.LUMINOUS_LAMP)
	public static Block LUMINOUS_LAMP;
	@ObjectHolder(BlockNames.LENS_WORKBENCH)
	public static Block LENS_WORKBENCH;
	@ObjectHolder(BlockNames.LASER_FILTER)
	public static Block LASER_FILTER;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {

	    @SubscribeEvent
	    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
	    	IForgeRegistry<Block> blockRegistry = event.getRegistry();
	    	
	    	blockRegistry.register(new BlockBasicLaser().setRegistryName(BlockNames.LASER_BASIC).setTranslationKey("lasermod.basic_laser"));
	    	blockRegistry.register(new BlockAdvancedLaser().setRegistryName(BlockNames.LASER_ADVANCED).setTranslationKey("lasermod.advanced_laser"));
	    	blockRegistry.register(new BlockLaserDetector().setRegistryName(BlockNames.LASER_DETECTOR).setTranslationKey("lasermod.detector"));
	    	blockRegistry.register(new BlockReflector().setRegistryName(BlockNames.REFLECTOR).setTranslationKey("lasermod.reflector"));
	    	blockRegistry.register(new BlockColourConverter().setRegistryName(BlockNames.COLOUR_CONVERTER).setTranslationKey("lasermod.colour_converter"));
	    	blockRegistry.register(new BlockSmallColourConverter().setRegistryName(BlockNames.COLOUR_CONVERTER_SMALL).setTranslationKey("lasermod.small_colour_converter"));
	    	blockRegistry.register(new BlockLuminousLamp().setRegistryName(BlockNames.LUMINOUS_LAMP).setTranslationKey("lasermod.luminous_lamp"));
	    	blockRegistry.register(new BlockLensWorkbench().setRegistryName(BlockNames.LENS_WORKBENCH).setTranslationKey("lasermod.lenswork_bench"));
	    	blockRegistry.register(new BlockLaserFilter().setRegistryName(BlockNames.LASER_FILTER).setTranslationKey("lasermod.laser_filter"));
	    	
	    	registerTileEntities();
	    }
	    
	    public static void registerTileEntities() {
			GameRegistry.registerTileEntity(TileEntityBasicLaser.class, BlockNames.LASER_BASIC);
			GameRegistry.registerTileEntity(TileEntityAdvancedLaser.class, BlockNames.LASER_ADVANCED);
			GameRegistry.registerTileEntity(TileEntityLaserDetector.class, BlockNames.LASER_DETECTOR);
			GameRegistry.registerTileEntity(TileEntityReflector.class, BlockNames.REFLECTOR);
			GameRegistry.registerTileEntity(TileEntityColourConverter.class, BlockNames.COLOUR_CONVERTER);
			GameRegistry.registerTileEntity(TileEntitySmallColourConverter.class, BlockNames.COLOUR_CONVERTER_SMALL);
			GameRegistry.registerTileEntity(TileEntityLuminousLamp.class, BlockNames.LUMINOUS_LAMP);
			GameRegistry.registerTileEntity(TileEntityLaserFilter.class, BlockNames.LASER_FILTER);
	    }
	    
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	       	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	       	
	       	itemRegistry.register(makeItemBlock(LASER_BASIC));
	       	itemRegistry.register(makeItemBlock(LASER_ADVANCED));
			itemRegistry.register(makeItemBlock(LASER_DETECTOR));
			itemRegistry.register(makeItemBlock(REFLECTOR));
			itemRegistry.register(makeItemBlock(COLOUR_CONVERTER));
			itemRegistry.register(makeItemBlock(COLOUR_CONVERTER_SMALL));
			itemRegistry.register(makeItemBlock(LUMINOUS_LAMP));
			itemRegistry.register(makeItemBlock(LENS_WORKBENCH));
			itemRegistry.register(makeItemBlock(LASER_FILTER));
	    }
	    
	    private static ItemBlock makeItemBlock(Block block) {
	        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
	    }
	}
}
