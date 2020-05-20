package net.dragonhill.wondrousmagitek.global;

import net.dragonhill.wondrousmagitek.global.chunkLoading.AreaStabilizerManager;

public final class ScopedServerServices {

	private static AreaStabilizerManager areaStabilizerManager;
	public static AreaStabilizerManager getAreaStabilizerManager() {
		return ScopedServerServices.areaStabilizerManager;
	}

	public static void init() {
		ScopedServerServices.areaStabilizerManager = new AreaStabilizerManager();
	}

	public static void teardown() {
		ScopedServerServices.areaStabilizerManager = null;
	}
}
