package lasermod.block;

import lasermod.LaserMod;
import lasermod.tileentity.TileEntityBasicLaser;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class BlockBasicLaser extends BlockPoweredRedstone {
	
	public BlockBasicLaser() {
		super(Material.ROCK);
		this.setHardness(1.0F);
		this.setCreativeTab(LaserMod.TAB_LASER);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) { 
		return new TileEntityBasicLaser(); 
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if(state.getValue(FACING) == face) return BlockFaceShape.UNDEFINED;
        return BlockFaceShape.SOLID;
    }
	
	/**
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {

		TileEntityBasicLaser basiclaser = (TileEntityBasicLaser)world.getTileEntity(pos);
    	
        if (world.isBlockIndirectlyGettingPowered(pos) == 0) {
        	BlockActionPos reciver = LaserUtil.getFirstBlock(basiclaser, EnumFacing.byIndex(basiclaser.getBlockMetadata()));
    		if(reciver != null && reciver.isLaserReceiver(EnumFacing.byIndex(basiclaser.getBlockMetadata()))) {
    			reciver.getLaserReceiver(EnumFacing.byIndex(basiclaser.getBlockMetadata())).removeLasersFromSide(world, pos, EnumFacing.byIndex(basiclaser.getBlockMetadata()).getOpposite());
    		}
        }
        else if (world.isBlockIndirectlyGettingPowered(pos) > 0) {
    		BlockActionPos reciver = LaserUtil.getFirstBlock(basiclaser, EnumFacing.byIndex(basiclaser.getBlockMetadata()));
    		if(reciver != null && reciver.isLaserReceiver(EnumFacing.byIndex(basiclaser.getBlockMetadata()))) {
    		  	
    			LaserInGame laserInGame = basiclaser.getOutputLaser(EnumFacing.byIndex(basiclaser.getBlockMetadata()));
    		  	
    		  	if(reciver.getLaserReceiver(EnumFacing.byIndex(basiclaser.getBlockMetadata())).canPassOnSide(world, pos, EnumFacing.byIndex(basiclaser.getBlockMetadata()).getOpposite(), laserInGame))
    		  		reciver.getLaserReceiver(EnumFacing.byIndex(basiclaser.getBlockMetadata())).passLaser(world, pos, EnumFacing.byIndex(basiclaser.getBlockMetadata()).getOpposite(), laserInGame);
    		}
    		else if(reciver != null) {
    			LaserInGame laserInGame = basiclaser.getOutputLaser(EnumFacing.byIndex(basiclaser.getBlockMetadata()));
    			
    			for(ILaser laser : laserInGame.getLaserType()) laser.actionOnBlock(reciver);
    		}
        }
	}**/
}
