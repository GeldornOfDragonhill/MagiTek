package net.dragonhill.wondrousmagitek.global.chunkLoading;

public class ActiveAreaStabilizerData {
	private final DimensionBlockPosition areaStabilizerPosition;
	private int radius;

	public int getRadius() {
		return this.radius;
	}

	public ActiveAreaStabilizerData(DimensionBlockPosition areaStabilizerPosition, int radius) {
		this.areaStabilizerPosition = areaStabilizerPosition;
		this.radius = radius;
	}
}
