package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.network.TileEntityCommandMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

public class SetTickNameCommandMessage extends TileEntityCommandMessage {

	private String name;

	public SetTickNameCommandMessage() { }

	public SetTickNameCommandMessage(TickContainer container) {
		super(container.getTileEntity());

		this.name = container.name.get();
	}

	@Override
	protected void handleCommand(ServerPlayerEntity sender, TileEntity tileEntity) {
		if(tileEntity instanceof TickTileEntity) {
			((TickTileEntity)tileEntity).setName(this.name);
		}
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		super.serialize(buffer);
		buffer.writeString(this.name);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		super.deserialize(buffer);
		this.name = buffer.readString(Short.MAX_VALUE); //TODO: maybe limit that to something reasonable
	}
}
