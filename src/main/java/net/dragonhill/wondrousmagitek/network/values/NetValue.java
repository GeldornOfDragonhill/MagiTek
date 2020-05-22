package net.dragonhill.wondrousmagitek.network.values;

import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;

public abstract class NetValue<T> implements INetValue {
	private boolean changed = true;
	private Consumer<T> callback;

	public boolean getChanged() {
		return this.changed;
	}

	protected void setChanged() {
		this.changed = true;
	}

	public void writeToBuffer(PacketBuffer buffer) {
		this.serialize(buffer);
		this.changed = false;
	}

	public void readFromBuffer(PacketBuffer buffer) {
		this.deserialize(buffer);
		this.changed = false;
	}

	public void registerCallback(Consumer<T> callback) {
		if(this.callback != null) {
			throw new IllegalStateException("Callback already registered");
		}

		this.callback = callback;
	}

	protected void executeCallback(T self) {
		if(this.callback != null) {
			this.callback.accept(self);
		}
	}

	protected abstract void serialize(PacketBuffer buffer);
	protected abstract void deserialize(PacketBuffer buffer);
}
