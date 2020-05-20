package net.dragonhill.wondrousmagitek.global.chunkLoading;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

import java.util.*;

public class TicketAllocator {
	private static final TicketType<ChunkPos> modTicket = TicketType.create(Constants.modId, Comparator.comparingLong(ChunkPos::asLong));

	private Map<Integer, Map<ChunkPos, List<ActiveAreaStabilizerData>>> tickets = new HashMap<>();

	public boolean hasDimensionTickets(ServerWorld world) {
		return this.tickets.containsKey(world.getDimension().getType().getId());
	}

	public void allocate(ServerWorld world, ChunkPos chunk, ActiveAreaStabilizerData instance) {
		final int dimensionId = world.getDimension().getType().getId();
		Map<ChunkPos, List<ActiveAreaStabilizerData>> dimensionTickets = this.tickets.computeIfAbsent(dimensionId, k -> {
			world.resetUpdateEntityTick(); //Just to be on the save side if the counter is already high
			return new HashMap<>();
		});

		List<ActiveAreaStabilizerData> claimHolders = dimensionTickets.computeIfAbsent(chunk, k -> new ArrayList<>());

		if(claimHolders.size() == 0) {
			world.getChunkProvider().registerTicket(TicketAllocator.modTicket, chunk, 2, chunk);
		}

		if(claimHolders.contains(instance)) {
			throw new RuntimeException("Ticket double claim");
		}

		claimHolders.add(instance);
	}

	public void free(ServerWorld world, ChunkPos chunk, ActiveAreaStabilizerData instance) {
		final int dimensionId = world.getDimension().getType().getId();

		Map<ChunkPos, List<ActiveAreaStabilizerData>> dimensionTickets = this.tickets.get(dimensionId);

		if(dimensionTickets == null) {
			throw new RuntimeException("Trying to free tickets in unused dimension");
		}

		List<ActiveAreaStabilizerData> claimHolders = dimensionTickets.get(chunk);

		if(claimHolders == null || !claimHolders.remove(instance)) {
			throw new RuntimeException("Trying to free unallocated ticket");
		}

		if(claimHolders.isEmpty()) {
			world.getChunkProvider().releaseTicket(TicketAllocator.modTicket, chunk, 2, chunk);
			dimensionTickets.remove(chunk);
		}

		if(dimensionTickets.isEmpty()) {
			this.tickets.remove(dimensionId);
		}
	}
}
