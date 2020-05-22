package net.dragonhill.wondrousmagitek.network.values;

import net.minecraft.network.PacketBuffer;

public class IntNetValue extends NetValue<IntNetValue> {
	private int value;

	public int get() {
		return this.value;
	}

	public void set(int value) {
		if(this.value != value) {
			this.setChanged();
			this.value = value;
		}
	}

	public IntNetValue() {

	}

	public IntNetValue(int value) {
		this.value = value;
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		buffer.writeInt(this.value);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		this.value = buffer.readInt();
		this.executeCallback(this);
	}
}
