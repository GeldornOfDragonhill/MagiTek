package net.dragonhill.wondrousmagitek.ui;

import net.dragonhill.wondrousmagitek.init.ModBlocks;
import net.dragonhill.wondrousmagitek.network.ModNetworkChannel;
import net.dragonhill.wondrousmagitek.network.values.INetValue;
import net.dragonhill.wondrousmagitek.network.values.INetValueListHolder;
import net.dragonhill.wondrousmagitek.network.values.NetValueList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public abstract class InventoryScreenContainer<TTileEntity extends TileEntity> extends Container implements INetValueListHolder {

	protected final TTileEntity tileEntity;
	protected final IWorldPosCallable canInteractWithCallable;

	protected Set<ServerPlayerEntity> listeners = new HashSet<>();

	public TTileEntity getTileEntity() {
		return this.tileEntity;
	}

	protected InventoryScreenContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, TTileEntity tileEntity) {
		super(type, id);

		this.registerNetValues();
		this.updateNetValues(tileEntity);

		this.tileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		this.setupStandardInventory(playerInventory);
	}

	protected static <T> T getTileEntity(final PlayerInventory inventory, final PacketBuffer data) {
		return (T) inventory.player.world.getTileEntity(data.readBlockPos());
	}

	private void setupStandardInventory(final PlayerInventory playerInventory) {

		final int xStart = 8;
		final int entrySize = 18;

		final int yStartHotBar = 62 + getClientHeight();

		//Add hot bar
		for(int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, xStart + column * entrySize, yStartHotBar));
		}

		final int yStartInventory = 4 + getClientHeight();

		//Add inventory
		for(int row = 0; row < 3; ++row) {
			final int currentY = yStartInventory + row * entrySize;
			final int rowInventoryOffset = (row + 1 ) * 9;

			for(int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, rowInventoryOffset + column, xStart + column * entrySize, currentY));
			}
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.isWithinUsableDistance(this.canInteractWithCallable, playerIn, ModBlocks.areaStabilizer.get());
	}

	@Override
	public void addListener(IContainerListener listener) {
		if(listener instanceof ServerPlayerEntity) {
			this.listeners.add((ServerPlayerEntity)listener);
		}
		super.addListener(listener);
	}

	@Override
	public void removeListener(IContainerListener listener) {
		if(listener instanceof ServerPlayerEntity) {
			this.listeners.remove(listener);
		}
		super.removeListener(listener);
	}

	protected abstract int getClientHeight();




	private final NetValueList netValueList = new NetValueList();

	protected <V extends INetValue> V addNetValue(V value) {
		return this.netValueList.add(value);
	}

	@Override
	public NetValueList getNetValueList() {
		return this.netValueList;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		this.updateNetValues(this.tileEntity);

		this.netValueList.createPayloadForChanged(payload -> {
			for(ServerPlayerEntity listener : this.listeners) {

				//TODO: can the same message be sent to all players?
				ModNetworkChannel.sendToPlayer(new ScreenClientUpdateMessage(this.windowId, payload), listener);
			}
		});
	}

	protected abstract void registerNetValues();
	protected abstract void updateNetValues(TTileEntity tileEntity);
}
