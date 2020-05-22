package net.dragonhill.wondrousmagitek.network.values;

import net.minecraft.network.PacketBuffer;

public interface INetValue {
	boolean getChanged();
	void writeToBuffer(PacketBuffer buffer);
	void readFromBuffer(PacketBuffer buffer);
}
