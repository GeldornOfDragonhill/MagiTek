package net.dragonhill.wondrousmagitek.network.values;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetValueList {
	private boolean wasUpdated = false;
	private final List<INetValue> netValues = new ArrayList<>();

	public boolean getWasUpdated() {
		return this.wasUpdated;
	}

	public <V extends INetValue> V add(V value) {
		this.netValues.add(value);
		return value;
	}

	public void createPayloadForChanged(Consumer<PacketBuffer> payloadConsumer) {
		boolean actionRequired = false;

		for (INetValue netValue: this.netValues) {
			if(netValue.getChanged()) {
				actionRequired = true;
				break;
			}
		}

		if(actionRequired) {
			final PacketBuffer payloadBuffer = new PacketBuffer(Unpooled.buffer());

			for (INetValue netValue: this.netValues) {
				netValue.writeToBuffer(payloadBuffer);
			}

			payloadConsumer.accept(payloadBuffer);
		}
	}

	public void updateFromPayload(PacketBuffer payload) {
		for (INetValue netValue : this.netValues) {
			netValue.readFromBuffer(payload);
		}

		this.wasUpdated = true;
	}
}
