package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.init.ModBlocks;
import net.dragonhill.wondrousmagitek.init.ModContainers;
import net.dragonhill.wondrousmagitek.network.values.StringNetValue;
import net.dragonhill.wondrousmagitek.ui.InventoryScreenContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class TickContainer  extends InventoryScreenContainer<TickTileEntity> {

	public StringNetValue name;

	public TickContainer(final int windowId, final PlayerInventory playerInventory, final TickTileEntity tileEntity) {
		super(ModContainers.TICK_CONTAINER.get(), windowId, playerInventory, tileEntity);
	}

	public TickContainer(final int windowId, final PlayerInventory inv, final PacketBuffer data) {
		this(windowId, inv, TickContainer.<TickTileEntity>getTileEntity(inv, data));
	}

	@Override
	protected int getClientHeight() {
		return 85;
	}

	@Override
	protected Block getBlock() {
		return ModBlocks.tickBlock.get();
	}

	@Override
	protected void registerNetValues() {
		this.name = this.addNetValue(new StringNetValue());
	}

	@Override
	protected void updateNetValues(TickTileEntity tileEntity) {
		this.name.set(tileEntity.getName());
	}
}
