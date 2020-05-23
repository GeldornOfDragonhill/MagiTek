package net.dragonhill.wondrousmagitek.network.values;

import net.minecraft.network.PacketBuffer;

public class StringNetValue extends NetValue<StringNetValue> {
	private String value;

	public String get() {
		return this.value;
	}

	public boolean set(String value) {
		if((this.value == null && value != null) || !this.value.equals(value)) {
			this.setChanged();
			this.value = value;

			return true;
		}

		return false;
	}

	public StringNetValue() {

	}

	public StringNetValue(String value) {
		this.value = value;
	}

	@Override
	protected void serialize(PacketBuffer buffer) {
		buffer.writeString(this.value);
	}

	@Override
	protected void deserialize(PacketBuffer buffer) {
		this.value = buffer.readString();
		this.executeCallback(this);
	}
}
