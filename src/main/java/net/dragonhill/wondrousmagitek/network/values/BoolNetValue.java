package net.dragonhill.wondrousmagitek.network.values;

import net.minecraft.network.PacketBuffer;

public class BoolNetValue extends NetValue {
	private boolean value;

	public boolean get() {
		return this.value;
	}

	public void set(boolean value) {
		if(this.value != value) {
			this.setChanged();
			this.value = value;
		}
	}

	public BoolNetValue() {

	}

	public BoolNetValue(boolean value) {
		this.value = value;
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		buffer.writeBoolean(this.value);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		this.value = buffer.readBoolean();
		this.executeCallback(this);
	}
}
