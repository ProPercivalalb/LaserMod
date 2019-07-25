package lasermod.network.packet.client;

import lasermod.api.LaserInGame;
import lasermod.network.AbstractMessage.AbstractClientMessage;
import lasermod.network.IPacket;
import lasermod.tileentity.TileEntityColourConverter;
import lasermod.tileentity.TileEntityLaserFilter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LaserFilterMessage extends AbstractClientMessage<LaserFilterMessage> implements IPacket<LaserFilterMessage>{

	public BlockPos pos;
    public LaserInGame laser;
    public EnumDyeColor colour;
	
    public LaserFilterMessage() {}
    public LaserFilterMessage(TileEntityLaserFilter entity) {
    	 this.pos = entity.getPos();
         this.laser = entity.laser;
         this.colour = entity.colour;
    }
    
	@Override
	public void encode(LaserFilterMessage msg, PacketBuffer buffer) {
		buffer.writeBlockPos(msg.pos);
		buffer.writeBoolean(msg.laser != null);
		if(msg.laser != null)
			msg.laser.writeToPacket(buffer);
		buffer.writeInt(msg.colour.getMetadata());
	}

	@Override
	public LaserFilterMessage decode(PacketBuffer buffer) {
		this.pos = buffer.readBlockPos();
		
		if(buffer.readBoolean())
			this.laser = LaserInGame.readFromPacket(buffer);
		this.colour = EnumDyeColor.byMetadata(buffer.readInt());
		return this;
	}

	@Override
	public void handle(LaserFilterMessage msg, EntityPlayer player) {
		World world = player.world;
		TileEntity tileEntity = world.getTileEntity(msg.pos);
		
		if(!(tileEntity instanceof TileEntityLaserFilter)) 
			return;
		TileEntityLaserFilter laserFilter = (TileEntityLaserFilter)tileEntity;
		laserFilter.laser = msg.laser;
		laserFilter.colour = msg.colour;
		world.markAndNotifyBlock(msg.pos, world.getChunk(msg.pos), world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
	}

}
