package net.dragonhill.wondrousmagitek.global.chunkLoading;

import net.dragonhill.wondrousmagitek.config.Config;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.global.ScopedState;
import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerTileEntity;
import net.dragonhill.wondrousmagitek.util.ConsumerDataSink;
import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaStabilizerManager {
	public static AreaStabilizerManager getInstance() {
		return ScopedState.getAreaStabilizerManager();
	}

	private boolean isLoaded = false;
	private AreaStabilizerGlobalStorage storage;
	private final TicketAllocator allocator = new TicketAllocator();
	private final Map<String, AreaStabilizerUserManager> userManagers = new HashMap<>();
	private final Map<DimensionBlockPosition, String> areaStabilizerOwnerMapping = new HashMap<>();

	public boolean hasDimensionTickets(ServerWorld world) {
		return this.allocator.hasDimensionTickets(world);
	}

	public void load(ServerWorld world) {
		if(this.isLoaded) {
			return;
		}

		//It seems there is no global storage location anymore, so store the data in the overworld
		DimensionSavedDataManager savedDataManager = world.getServer().getWorld(DimensionType.OVERWORLD).getSavedData();

		//The dataLoadedAction callback get's called while the next call is active, so need to set it to a temporary callback first
		ConsumerDataSink<List<DimensionBlockPosition>> tempConsumer = new ConsumerDataSink<>();
		this.storage = savedDataManager.getOrCreate(() -> new AreaStabilizerGlobalStorage(Constants.chunkLoadingDataSection, tempConsumer, () -> getAllPositionsForStorage()), Constants.chunkLoadingDataSection);

		this.isLoaded = true;

		this.storage.setDataLoadedAction((positions) -> this.updateDimensionsFromStorage(positions));

		List<DimensionBlockPosition> initValue = tempConsumer.getLastValue();
		if(initValue != null) {
			this.updateDimensionsFromStorage(initValue);
		}
	}

	public void updatePlayerState(PlayerEntity player, boolean isOnline) {
		String name = player.getName().getString();
		AreaStabilizerUserManager userManager = userManagers.get(name);

		//If the player has currently no area stabilizers - nothing to do
		if(userManager == null) {
			return;
		}

		userManager.setOnlineStatus(player.world.getServer(), isOnline);
	}

	private void updateDimensionsFromStorage(List<DimensionBlockPosition> positions) {

		LogHelper.getLogger().info(String.format("Trying to load %d chunk stabilizers from storage", positions.size()));

		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		for(DimensionBlockPosition position : positions) {
			DimensionType dimensionType = DimensionType.getById(position.getDimensionId());

			if(dimensionType == null) {
				continue;
			}

			ServerWorld world = server.getWorld(dimensionType);

			if(world == null) {
				continue;
			}

			TileEntity tileEntity = world.getTileEntity(position.getPos());

			if(tileEntity instanceof AreaStabilizerTileEntity) {
				this.addOrUpdate((AreaStabilizerTileEntity)tileEntity);
			}
		}
	}

	private List<DimensionBlockPosition> getAllPositionsForStorage() {
		List<DimensionBlockPosition> positions = new ArrayList<>();

		for(AreaStabilizerUserManager userManager : this.userManagers.values()) {
			positions.addAll(userManager.getPositions());
		}

		return positions;
	}

	public void addOrUpdate(AreaStabilizerTileEntity tileEntity) {
		String newOwner = tileEntity.getOwner();

		//TODO: possibly optimize the dirty marking
		this.storage.markDirty();

		//Ownerless area stabilizers are not supported
		if(newOwner == null) {
			this.remove(tileEntity);
			return;
		}

		DimensionBlockPosition pos = new DimensionBlockPosition(tileEntity.getPos(), tileEntity.getWorld().getDimension().getType().getId());

		String currentOrNewOwner = this.areaStabilizerOwnerMapping.computeIfAbsent(pos, k -> newOwner);
		MinecraftServer server = tileEntity.getWorld().getServer();

		//Shouldn't happen at all, but to be on the save side
		if(!currentOrNewOwner.equals(newOwner)) {
			AreaStabilizerUserManager userManager = userManagers.get(currentOrNewOwner);

			if(userManager == null) {
				LogHelper.getLogger().error(String.format("Didn't find a user manager for a mapped area stabilizer %s", pos.toString()));
			} else {
				userManager.remove(server, pos, tileEntity);
			}
		}

		AreaStabilizerUserManager userManager = this.userManagers.computeIfAbsent(newOwner, k -> {
			PlayerEntity entity = server.getPlayerList().getPlayerByUsername(k);
			//TODO: Load max chunks from config file
			return new AreaStabilizerUserManager(this.allocator, k, Config.SERVER.maxTicketsPerPlayer.get(),entity != null && !entity.isSpectator());
		});

		userManager.addOrUpdate(server,pos,tileEntity);
	}

	public void remove(AreaStabilizerTileEntity tileEntity) {
		DimensionBlockPosition pos = new DimensionBlockPosition(tileEntity.getPos(), tileEntity.getWorld().getDimension().getType().getId());

		String playerName = this.areaStabilizerOwnerMapping.get(pos);

		if(playerName == null) {
			return;
		}

		this.storage.markDirty();

		this.areaStabilizerOwnerMapping.remove(pos);

		AreaStabilizerUserManager userManager = userManagers.get(playerName);

		if(userManager == null) {
			LogHelper.getLogger().error(String.format("Didn't find a user manager for a mapped area stabilizer %s", pos.toString()));
			return;
		}

		userManager.remove(tileEntity.getWorld().getServer(), pos, tileEntity);

		if(userManager.isEmpty()) {
			this.userManagers.remove(playerName);
		}
	}
}
