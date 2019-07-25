package lasermod.block;

import lasermod.LaserMod;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.LaserFilterMessage;
import lasermod.tileentity.TileEntityLaserFilter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLaserFilter extends BlockPoweredLaser {

	public BlockLaserFilter() {
		super(Material.IRON);
		this.setHardness(1.0F);
		this.setCreativeTab(LaserMod.TAB_LASER);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TileEntityLaserFilter();
	}
	

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() == Items.DYE) {
			if(!world.isRemote) {
				TileEntityLaserFilter laserFilter = (TileEntityLaserFilter)world.getTileEntity(pos);
				EnumDyeColor colour = EnumDyeColor.byDyeDamage(stack.getItemDamage());
				
				if(colour == laserFilter.colour) return true;
				
				laserFilter.colour = colour;
				if(!player.capabilities.isCreativeMode) stack.shrink(1);
				
				PacketDispatcher.sendToAllTracking(new LaserFilterMessage(laserFilter), laserFilter);
			}
			return true;
		}
		return false;
	}

}
