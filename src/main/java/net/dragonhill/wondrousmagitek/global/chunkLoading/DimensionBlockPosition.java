package net.dragonhill.wondrousmagitek.global.chunkLoading;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class DimensionBlockPosition {
	private final BlockPos pos;
	private int dimensionId;

	public final BlockPos getPos() {
		return this.pos;
	}

	public final int getDimensionId() {
		return this.dimensionId;
	}

	public DimensionBlockPosition(BlockPos pos, int dimensionId) {
		this.pos = pos;
		this.dimensionId = dimensionId;
	}

	public final void writeToNBT(CompoundNBT compound) {
		compound.putInt("x", this.pos.getX());
		compound.putInt("y", this.pos.getY());
		compound.putInt("z", this.pos.getZ());
		compound.putInt("d", this.dimensionId);
	}

	public final static DimensionBlockPosition readFromNBT(CompoundNBT compound) {
		int x = compound.getInt("x");
		int y = compound.getInt("y");
		int z = compound.getInt("z");
		int d = compound.getInt("d");

		return new DimensionBlockPosition(new BlockPos(x, y, z), d);
	}

	@Override
	public int hashCode() {
		return 31 * pos.hashCode() + dimensionId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DimensionBlockPosition)) {
			return false;
		} else {
			DimensionBlockPosition worldPos = (DimensionBlockPosition)obj;
			return this.dimensionId == worldPos.dimensionId && this.pos.getX() == worldPos.pos.getX() && this.pos.getY() == worldPos.pos.getY() && this.pos.getZ() == worldPos.pos.getZ();
		}
	}

	@Override
	public String toString() {
		return String.format("DIM[%d]@%s", this.dimensionId, this.pos.toString());
	}
}
