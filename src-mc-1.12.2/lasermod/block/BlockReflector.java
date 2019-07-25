package lasermod.block;

import java.util.Random;

import lasermod.LaserMod;
import lasermod.ModItems;
import lasermod.tileentity.TileEntityReflector;
import lasermod.util.LaserUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class BlockReflector extends BlockContainer {

	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockReflector() {
		super(Material.ROCK);
		this.setHardness(1.0F);
		this.setCreativeTab(LaserMod.TAB_LASER);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) { return new TileEntityReflector(); }

    @Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		
		if(stack.getItem() == ModItems.SCREWDRIVER) {
			if(!world.isRemote) {
				TileEntityReflector reflector = (TileEntityReflector)world.getTileEntity(pos);
				reflector.sideClosed[side.ordinal()] = !reflector.sideClosed[side.getIndex()];
				
				if(reflector.sideClosed[side.ordinal()])
					reflector.removeAllLasersFromSide(side);
				
				if(state.getValue(POWERED) && !this.isLaserSource(world, pos)) {
					world.scheduleUpdate(pos, this, 4);
					//LaserMod.LOGGER.debug("off");
				}
				else if(!state.getValue(POWERED) && this.isLaserSource(world, pos)) 
					cycleState(world, pos, state);
				else
				world.markAndNotifyBlock(pos, world.getChunk(pos), state, state, 2);
				//PacketDispatcher.sendToAllTracking(new ReflectorMessage(reflector), reflector);
			}
			return true;
		}
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) { return false; }
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, meta % 2 != 0);
    }

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		
		if(state.getValue(POWERED).booleanValue())
			meta = 1;

		return meta;
	}

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {POWERED});
    }
    
    public boolean isLaserSource(World world, BlockPos pos) {
    	TileEntity te = world.getTileEntity(pos);
    	if(!(te instanceof TileEntityReflector))
    		return false;
    	
    	TileEntityReflector ter = (TileEntityReflector)te;
    	
    	for(EnumFacing facing : EnumFacing.VALUES) {
    		if(!ter.sideClosed[facing.ordinal()]) {
    			if(LaserUtil.isValidSourceOfPowerOnSide(ter, facing)) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!worldIn.isRemote) {
			if(state.getValue(POWERED) && !this.isLaserSource(worldIn, pos))
				worldIn.scheduleUpdate(pos, this, 4);
			else if(!state.getValue(POWERED) && this.isLaserSource(worldIn, pos))
				cycleState(worldIn, pos, state);

		}
	}

    @Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote)
			if(state.getValue(POWERED) && !this.isLaserSource(world, pos))
				cycleState(world, pos, state);
			else if(!state.getValue(POWERED) && this.isLaserSource(world, pos))
				cycleState(world, pos, state);
		
	}
    
    public static void cycleState(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        worldIn.setBlockState(pos, state.cycleProperty(POWERED), 3);

        if(tileentity != null){
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    
    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    	if(w.getBlockState(pos).getBlock() instanceof BlockReflector) {
        	TileEntityReflector newEntity = (TileEntityReflector) w.getTileEntity(pos);
        	for(int i = 0; i < 6; i++) {
        		EnumFacing dir = EnumFacing.VALUES[i];
        		BlockPos oldPos = pos.offset(dir);
        		if(w.getBlockState(oldPos).getBlock() instanceof BlockReflector) {
        			TileEntityReflector oldEntity = (TileEntityReflector) w.getTileEntity(oldPos);
        			if(!oldEntity.sideClosed[dir.getOpposite().getIndex()]) {
        				newEntity.sideClosed[dir.getIndex()] = false;
        				w.scheduleUpdate(pos, this, 4);
        				newEntity.validate();
        				w.markAndNotifyBlock(pos, w.getChunk(pos), w.getBlockState(pos), w.getBlockState(pos), 2);
        			}
        		}
        	}
    	}
    }
}
