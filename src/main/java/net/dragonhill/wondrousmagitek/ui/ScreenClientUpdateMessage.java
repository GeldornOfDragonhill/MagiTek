package net.dragonhill.wondrousmagitek.ui;

import net.dragonhill.wondrousmagitek.network.MessageBase;
import net.dragonhill.wondrousmagitek.network.values.INetValueListHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ScreenClientUpdateMessage extends MessageBase {
	private int windowId;
	private PacketBuffer payload;

	public ScreenClientUpdateMessage() {

	}

	public ScreenClientUpdateMessage(int windowId, PacketBuffer payload) {
		this.windowId = windowId;
		this.payload = payload;
	}

	@Override
	protected void onExecuteInSidedThread(NetworkEvent.Context context) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		Container currentContainer = player.openContainer;

		if(currentContainer.windowId != this.windowId) {
			return;
		}

		if(currentContainer instanceof INetValueListHolder) {
			((INetValueListHolder)currentContainer).getNetValueList().updateFromPayload(this.payload);
		}
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		buffer.writeInt(this.windowId);
		buffer.writeBytes(this.payload);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		this.windowId = buffer.readInt();
		this.payload = buffer;
	}

	@Override
	protected LogicalSide getExecutionSide() {
		return LogicalSide.CLIENT;
	}
}
