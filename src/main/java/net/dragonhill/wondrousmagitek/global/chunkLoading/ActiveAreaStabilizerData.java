package net.dragonhill.wondrousmagitek.global.chunkLoading;

public class ActiveAreaStabilizerData {
	private final DimensionBlockPosition areaStabilizerPosition;
	private int radius;

	public int getRadius() {
		return this.radius;
	}

	protected void setRadius(int radius) {
		this.radius = radius;
	}

	public ActiveAreaStabilizerData(DimensionBlockPosition areaStabilizerPosition, int radius) {
		this.areaStabilizerPosition = areaStabilizerPosition;
		this.radius = radius;
	}
}
