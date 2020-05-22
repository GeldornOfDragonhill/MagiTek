package net.dragonhill.wondrousmagitek.global.chunkLoading;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.*;
import java.util.function.Function;

public class CoordinateHelper {
	public static final <T extends Collection<ChunkPos>> T getChunksFromPosAndRadius(BlockPos pos, int radius, Function<Integer, T> createCollectionWithSize) {
		final int chunkX = pos.getX() >> 4;
		final int chunkY = pos.getY() >> 4;

		final int xStart = chunkX - radius;
		final int xStop = chunkX + radius;
		final int zStart = chunkY - radius;
		final int zStop = chunkY + radius;

		int numChunks = 2 * radius + 1;
		numChunks = numChunks * numChunks;

		T chunks = createCollectionWithSize.apply(numChunks);

		for(int x = xStart; x <= xStop; ++x) {
			for(int z = zStart; z <= zStop; ++z) {
				chunks.add(new ChunkPos(x, z));
			}
		}

		return chunks;
	}

	public static final List<ChunkPos> getChunksAsListFromPosAndRadius(BlockPos pos, int radius) {
		return CoordinateHelper.getChunksFromPosAndRadius(pos, radius, count -> new ArrayList<ChunkPos>(count));
	}

	public static final Set<ChunkPos> getChunksAsSetFromPosAndRadius(BlockPos pos, int radius) {
		return CoordinateHelper.getChunksFromPosAndRadius(pos, radius, count -> new HashSet<ChunkPos>(count));
	}

	public static final long BlockPosToChunkLong(BlockPos pos) {
		return ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
	}
}
