package net.dragonhill.wondrousmagitek.global;

import net.dragonhill.wondrousmagitek.global.chunkLoading.AreaStabilizerManager;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.ChunkPos;

public final class ScopedState {

	private static AreaStabilizerManager areaStabilizerManager;
	public static AreaStabilizerManager getAreaStabilizerManager() {
		return ScopedState.areaStabilizerManager;
	}

	public static Tuple<ChunkPos, Integer> areaStabilizerVisualization;

	public static void init() {
		ScopedState.areaStabilizerManager = new AreaStabilizerManager();
		ScopedState.areaStabilizerVisualization = null;
	}

	public static void teardown() {
		ScopedState.areaStabilizerManager = null;
		ScopedState.areaStabilizerVisualization = null;
	}
}
