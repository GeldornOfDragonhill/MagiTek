package net.dragonhill.wondrousmagitek.network;

import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class MessageBase {
	protected abstract void serialize(final PacketBuffer buffer);
	protected abstract void deserialize(final PacketBuffer buffer);
	protected abstract LogicalSide getExecutionSide();

	protected void onExecuteInSidedThread(final NetworkEvent.Context context) { }

	protected void onExecute(final NetworkEvent.Context context) {
		context.enqueueWork(() -> this.onExecuteInSidedThread(context));
	}

	protected void onExecute(Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.setPacketHandled(true);
		if(!context.getDirection().getReceptionSide().equals(this.getExecutionSide())) {
			LogHelper.getLogger().warn("Received {} at wrong side!", this.getClass().getName());
			context.setPacketHandled(true); //TODO: should this be set to false for discarded packages?
			return;
		}
		this.onExecute(context);
		context.setPacketHandled(true);
	}
}
