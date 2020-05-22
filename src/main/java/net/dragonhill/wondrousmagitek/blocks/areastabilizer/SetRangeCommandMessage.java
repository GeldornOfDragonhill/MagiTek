package net.dragonhill.wondrousmagitek.blocks.areastabilizer;

import net.dragonhill.wondrousmagitek.network.TileEntityCommandMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

public class SetRangeCommandMessage extends TileEntityCommandMessage {

	private int radius;

	public SetRangeCommandMessage() { }

	public SetRangeCommandMessage(AreaStabilizerContainer container) {
		super(container.getTileEntity());

		this.radius = container.radius.get();
	}

	@Override
	protected void handleCommand(ServerPlayerEntity sender, TileEntity tileEntity) {
		if(tileEntity instanceof AreaStabilizerTileEntity) {
			((AreaStabilizerTileEntity)tileEntity).setRadius(sender, this.radius);
		}
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		super.serialize(buffer);
		buffer.writeInt(this.radius);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		super.deserialize(buffer);
		this.radius = buffer.readInt();
	}
}
