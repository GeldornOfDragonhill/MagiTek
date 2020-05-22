package net.dragonhill.wondrousmagitek.network;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ModNetworkChannel {
	private static final String PROTOCOL_VERSION = "1";
	private final static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.modId, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	private static int currentMessageId = 0;

	public static <T extends MessageBase> void registerMessage(final Class<T> type, final Supplier<T> factory) {
		synchronized(CHANNEL) {
			CHANNEL.<T>registerMessage(++currentMessageId, type, T::serialize, buf -> {
				final T msg = factory.get();
				msg.deserialize(buf);
				return msg;
			}, T::onExecute);
		}
	}

	public static void sendToPlayer(final MessageBase msg, final ServerPlayerEntity player) {
		ModNetworkChannel.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
	}

	public static void sendToServer(final MessageBase msg) {
		ModNetworkChannel.CHANNEL.sendToServer(msg);
	}
}
