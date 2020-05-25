package net.dragonhill.wondrousmagitek.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
	public static class ServerConfig {

		public final IntValue maxTicketsPerPlayer;
		public final IntValue maxRadiusPerAreaStabilizer;

		private ServerConfig(final ForgeConfigSpec.Builder builder) {
			builder.comment("AreaStabilizer settings (chunk loading)")
				.push("areaStabilizer");

			this.maxTicketsPerPlayer = builder
				.comment("The maximum number of tickets (loaded chunks) a player could claim")
				.defineInRange("maxTicketsPerPlayer", 400, 0, Integer.MAX_VALUE);

			this.maxRadiusPerAreaStabilizer = builder
				.comment("The maximum radius for an area stabilizer (0 = just the chunk it is in)")
				.defineInRange("maxRadius", 9, 0, 25);

			builder.pop();
		}
	}

	private static final ForgeConfigSpec serverSpec;
	public static final ServerConfig SERVER;

	static {
		final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static void register(final ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.SERVER, serverSpec);
	}
}
