package net.dragonhill.wondrousmagitek;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.global.ScopedState;
import net.dragonhill.wondrousmagitek.global.chunkLoading.AreaStabilizerManager;
import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerBoundsRenderer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod.EventBusSubscriber(modid = Constants.modId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventDispatcher {
	@SubscribeEvent
	public static final void onServerAboutToStart(FMLServerAboutToStartEvent event) {
		ScopedState.init();
	}

	@SubscribeEvent
	public static final void onServerStoppedEvent(FMLServerStoppedEvent event) {
		ScopedState.teardown();
	}

	@SubscribeEvent
	public static final void onTick(TickEvent.WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
			ServerWorld world = (ServerWorld)event.world;

			//Only do the dimension check/reset every 128 ticks (should be enough)
			if((world.getGameTime() & 0x7F) == 0) {
				if(AreaStabilizerManager.getInstance().hasDimensionTickets(world)) {
					world.resetUpdateEntityTick();
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		if(ScopedState.areaStabilizerVisualization != null) {
			AreaStabilizerBoundsRenderer.render(event, ScopedState.areaStabilizerVisualization);
		}
	}

	@SubscribeEvent
	public static final void onWorldLoad(WorldEvent.Load event) {
		if(event.getWorld().isRemote()) {
			return;
		}

		AreaStabilizerManager.getInstance().load((ServerWorld)event.getWorld());
	}

	@SubscribeEvent
	public static final void onPlayerConnected(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.getPlayer().isSpectator()) {
			return;
		}

		AreaStabilizerManager.getInstance().updatePlayerState(event.getPlayer(), true);
	}

	@SubscribeEvent
	public static final void onPlayerDisconnected(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.getPlayer().isSpectator()) {
			return;
		}

		AreaStabilizerManager.getInstance().updatePlayerState(event.getPlayer(), false);
	}
}
