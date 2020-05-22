package net.dragonhill.wondrousmagitek.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public abstract class TileEntityCommandMessage<TTileEntity extends TileEntity> extends MessageBase {

	private int dimensionId;
	private BlockPos blockPos;

	public TileEntityCommandMessage() {

	}

	public TileEntityCommandMessage(TileEntity tileEntity) {
		this.dimensionId = tileEntity.getWorld().getDimension().getType().getId();
		this.blockPos = tileEntity.getPos();
	}

	@Override
	protected void onExecuteInSidedThread(NetworkEvent.Context context) {
		ServerPlayerEntity sender = context.getSender();
		World senderWorld = sender.world;

		//Check if the sender is in the correct dimension
		if(senderWorld.dimension.getType().getId() != this.dimensionId) {
			return;
		}

		//Check if the block is loaded
		if(!senderWorld.isBlockLoaded(this.blockPos)) {
			return;
		}

		this.handleCommand(sender, senderWorld.getTileEntity(this.blockPos));
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		buffer.writeInt(this.dimensionId);
		buffer.writeBlockPos(this.blockPos);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		this.dimensionId = buffer.readInt();
		this.blockPos = buffer.readBlockPos();
	}

	@Override
	protected LogicalSide getExecutionSide() {
		return LogicalSide.SERVER;
	}

	protected abstract void handleCommand(ServerPlayerEntity sender, TileEntity tileEntity);
}
