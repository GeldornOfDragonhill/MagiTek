package net.dragonhill.wondrousmagitek.global.chunkLoading;

import com.google.common.collect.Sets;
import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerTileEntity;
import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AreaStabilizerUserManager {

	private final TicketAllocator allocator;
	private final String playerName;
	private final int maxChunks;
	private int currentChunks;

	private boolean isCurrentlyOnline;
	private boolean hasLoadersOverLimit = false;
	private final Map<DimensionBlockPosition, ActiveAreaStabilizerData> ownedAreaStabilizers = new HashMap<>();

	public boolean isEmpty() {
		return this.ownedAreaStabilizers.isEmpty();
	}

	public Set<DimensionBlockPosition> getPositions() {
		return this.ownedAreaStabilizers.keySet();
	}

	public AreaStabilizerUserManager(TicketAllocator allocator, String playerName, int maxChunks, boolean isOnline) {
		this.allocator = allocator;
		this.playerName = playerName;
		this.maxChunks = maxChunks;
		this.isCurrentlyOnline = isOnline;
	}

	public void setOnlineStatus(MinecraftServer server, boolean isOnline) {
		if(this.isCurrentlyOnline == isOnline) {
			return;
		}

		if(isOnline) {
			this.tryActivateAll(server);
		} else {
			this.deactivateAll(server);
			this.hasLoadersOverLimit = false;
		}

		this.isCurrentlyOnline = isOnline;
	}

	public void addOrUpdate(MinecraftServer server, DimensionBlockPosition pos, AreaStabilizerTileEntity tileEntity) {
		//If the target player is currently not online, just check if the position is known
		if(!this.isCurrentlyOnline) {
			this.ownedAreaStabilizers.computeIfAbsent(pos, k -> null);
			return;
		}

		ActiveAreaStabilizerData data = this.ownedAreaStabilizers.get(pos);

		if(data != null) {
			if(data.getRadius() == tileEntity.getRadius()) {
				//Nothing changed
				return;
			}

			Set<ChunkPos> oldChunks = CoordinateHelper.getChunksAsSetFromPosAndRadius(pos.getPos(), data.getRadius());
			Set<ChunkPos> newChunks = CoordinateHelper.getChunksAsSetFromPosAndRadius(pos.getPos(), tileEntity.getRadius());

			int sizeDifference = newChunks.size() - oldChunks.size();

			if(this.currentChunks + sizeDifference > this.maxChunks) {
				this.deactivate(server, pos);
				this.hasLoadersOverLimit = true;
				this.sendMessageToPlayer(server, "message.wondrousmagitek.area_stabilizer.over_limit", pos);
				return;
			}

			Iterable<ChunkPos> removedChunks = Sets.difference(oldChunks, newChunks);
			Iterable<ChunkPos> addedChunks = Sets.difference(newChunks, oldChunks);

			for(ChunkPos chunk : removedChunks) {
				allocator.free((ServerWorld)tileEntity.getWorld(), chunk, data);
			}

			for(ChunkPos chunk : addedChunks) {
				allocator.allocate((ServerWorld)tileEntity.getWorld(), chunk, data);
			}

			this.currentChunks += sizeDifference;

			//If size was reduced and there are area stabilizers that are not loaded, reevaluate
			if(this.hasLoadersOverLimit && sizeDifference < 0) {
				this.tryActivateAll(server);
			}
		} else { //New area stabilizer
			this.ownedAreaStabilizers.put(pos, null);

			//Important: use the version with the tileEntity here
			this.tryActivate(server, pos, tileEntity, true);
		}
	}

	public void remove(MinecraftServer server, DimensionBlockPosition pos, AreaStabilizerTileEntity tileEntity) {
		this.deactivate(server, pos);
		this.ownedAreaStabilizers.remove(pos);

		if(this.hasLoadersOverLimit) {
			this.tryActivateAll(server);
		}
	}

	private void tryActivateAll(MinecraftServer server) {
		boolean oldHasLoadersOverLimit = this.hasLoadersOverLimit;
		this.hasLoadersOverLimit = false;

		for(Map.Entry<DimensionBlockPosition, ActiveAreaStabilizerData> entry : this.ownedAreaStabilizers.entrySet()) {

			//Don't activate again
			if(entry.getValue() != null) {
				continue;
			}

			this.tryActivate(server, entry.getKey(), false);
		}

		if(oldHasLoadersOverLimit && !this.hasLoadersOverLimit) {
			this.sendMessageToPlayer(server, "message.wondrousmagitek.area_stabilizer.below_limit");
		}
	}

	private void tryActivate(MinecraftServer server, DimensionBlockPosition areaStabilizerPos, boolean forceSendLimitMessage) {

		DimensionType dimensionType = DimensionType.getById(areaStabilizerPos.getDimensionId());

		if (dimensionType == null) {
			LogHelper.getLogger().error(String.format("Couldn't load dimension %d, removing area stabilizer", areaStabilizerPos.getDimensionId()));
			this.ownedAreaStabilizers.remove(areaStabilizerPos);
			return;
		}

		ServerWorld world = server.getWorld(dimensionType);

		if (world == null) {
			LogHelper.getLogger().error(String.format("Couldn't load world %s, removing area stabilizer", world.toString()));
			this.ownedAreaStabilizers.remove(areaStabilizerPos);
			return;
		}

		TileEntity tileEntity = world.getTileEntity(areaStabilizerPos.getPos());

		if(!(tileEntity instanceof AreaStabilizerTileEntity)) {
			LogHelper.getLogger().error(String.format("Couldn't get tile entity for area stabilizer @%s, removing area stabilizer", areaStabilizerPos.toString()));
			this.ownedAreaStabilizers.remove(areaStabilizerPos);
			return;
		}

		this.tryActivate(server, areaStabilizerPos, (AreaStabilizerTileEntity)tileEntity, forceSendLimitMessage);
	}

	//Need to split that method because getting the tile entity while the tile entity is in the onLoad method seems to be quite problematic
	private void tryActivate(MinecraftServer server, DimensionBlockPosition areaStabilizerPos, AreaStabilizerTileEntity areaStabilizerTileEntity, boolean forceSendLimitMessage) {

		List<ChunkPos> chunks = CoordinateHelper.getChunksAsListFromPosAndRadius(areaStabilizerTileEntity.getPos(), areaStabilizerTileEntity.getRadius());

		if(chunks.size() > (this.maxChunks - this.currentChunks)) {
			if(!this.hasLoadersOverLimit || forceSendLimitMessage) {
				this.sendMessageToPlayer(server, "message.wondrousmagitek.area_stabilizer.over_limit", areaStabilizerPos);
			}
			this.hasLoadersOverLimit = true;
			return;
		}

		ActiveAreaStabilizerData data = new ActiveAreaStabilizerData(areaStabilizerPos, areaStabilizerTileEntity.getRadius());

		for(ChunkPos chunk : chunks) {
			allocator.allocate((ServerWorld)areaStabilizerTileEntity.getWorld(), chunk, data);
		}

		this.currentChunks += chunks.size();
		this.ownedAreaStabilizers.replace(areaStabilizerPos, data);
	}

	private void deactivateAll(MinecraftServer server) {
		for(DimensionBlockPosition pos : this.ownedAreaStabilizers.keySet()) {
			this.deactivate(server, pos);
		}
	}

	private void deactivate(MinecraftServer server, DimensionBlockPosition areaStabilizerPos) {
		ActiveAreaStabilizerData data = this.ownedAreaStabilizers.get(areaStabilizerPos);

		//Not active, nothing to deactivate
		if(data == null) {
			return;
		}

		DimensionType dimensionType = DimensionType.getById(areaStabilizerPos.getDimensionId());

		if(dimensionType == null) {
			throw new IllegalStateException(String.format("Could not get dimension type (DIM:%d) for already used dimension", areaStabilizerPos.getDimensionId()));
		}

		ServerWorld world = server.getWorld(dimensionType);

		if(world == null) {
			throw new IllegalStateException(String.format("Couldn't load world %s for already used dimension", world.toString()));
		}

		List<ChunkPos> chunks = CoordinateHelper.getChunksAsListFromPosAndRadius(areaStabilizerPos.getPos(), data.getRadius());

		for(ChunkPos chunkPos: chunks) {
			allocator.free(world, chunkPos, data);
		}

		this.currentChunks -= chunks.size();

		this.ownedAreaStabilizers.replace(areaStabilizerPos, null);
	}

	private void sendMessageToPlayer(MinecraftServer server, String translationKey, Object... args) {
		ServerPlayerEntity player = server.getPlayerList().getPlayerByUsername(this.playerName);

		if(player == null) {
			LogHelper.getLogger().error(String.format("Player %s is not available but should be", this.playerName));
			return;
		}

		player.sendMessage(new TranslationTextComponent(translationKey, args));
	}
}
