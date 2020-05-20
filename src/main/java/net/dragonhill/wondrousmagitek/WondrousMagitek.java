package net.dragonhill.wondrousmagitek;

import net.dragonhill.wondrousmagitek.config.Config;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.init.ModBlocks;
import net.dragonhill.wondrousmagitek.init.ModItems;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.modId)
public class WondrousMagitek {
	public WondrousMagitek() {
		Config.register(ModLoadingContext.get());

		final IEventBus eventBus = MinecraftForge.EVENT_BUS;

		eventBus.register(GlobalEventDispatcher.class);

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModBlocks.blocks.register(modEventBus);
		ModItems.items.register(modEventBus);
		ModTileEntities.tileEntities.register(modEventBus);
	}
}
