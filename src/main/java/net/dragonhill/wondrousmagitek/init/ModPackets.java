package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.blocks.areastabilizer.SetRangeCommandMessage;
import net.dragonhill.wondrousmagitek.blocks.tick.SetTickNameCommandMessage;
import net.dragonhill.wondrousmagitek.network.ModNetworkChannel;
import net.dragonhill.wondrousmagitek.ui.ScreenClientUpdateMessage;

public class ModPackets {
	public static void registerPackets() {
		ModNetworkChannel.registerMessage(ScreenClientUpdateMessage.class, ScreenClientUpdateMessage::new);
		ModNetworkChannel.registerMessage(SetRangeCommandMessage.class, SetRangeCommandMessage::new);
		ModNetworkChannel.registerMessage(SetTickNameCommandMessage.class, SetTickNameCommandMessage::new);
	}
}
