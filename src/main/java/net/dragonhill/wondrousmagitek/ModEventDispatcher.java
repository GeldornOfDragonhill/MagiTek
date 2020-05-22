package net.dragonhill.wondrousmagitek;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.init.ModPackets;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.modId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventDispatcher {

	@SubscribeEvent
	public static void onCommonSetup(final FMLCommonSetupEvent event) {
		ModPackets.registerPackets();
	}
}
