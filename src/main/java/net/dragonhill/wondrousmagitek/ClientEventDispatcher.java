package net.dragonhill.wondrousmagitek;

import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerScreen;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.init.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.modId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventDispatcher {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		//TODO: get this into init package
		ScreenManager.registerFactory(ModContainers.AREA_STABILIZER_CONTAINER.get(), AreaStabilizerScreen::new);
	}
}
