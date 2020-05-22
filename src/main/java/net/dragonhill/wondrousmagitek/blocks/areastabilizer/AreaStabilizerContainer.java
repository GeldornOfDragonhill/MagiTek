package net.dragonhill.wondrousmagitek.blocks.areastabilizer;

import net.dragonhill.wondrousmagitek.config.Config;
import net.dragonhill.wondrousmagitek.init.ModContainers;
import net.dragonhill.wondrousmagitek.network.values.IntNetValue;
import net.dragonhill.wondrousmagitek.ui.InventoryScreenContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class AreaStabilizerContainer extends InventoryScreenContainer<AreaStabilizerTileEntity> {

	public IntNetValue radius;
	public IntNetValue maxRadius;

	public AreaStabilizerContainer(final int windowId, final PlayerInventory playerInventory, final AreaStabilizerTileEntity tileEntity) {
		super(ModContainers.AREA_STABILIZER_CONTAINER.get(), windowId, playerInventory, tileEntity);
	}

	public AreaStabilizerContainer(final int windowId, final PlayerInventory inv, final PacketBuffer data) {
		this(windowId, inv, AreaStabilizerContainer.<AreaStabilizerTileEntity>getTileEntity(inv, data));
	}

	@Override
	protected int getClientHeight() {
		return 85;
	}

	@Override
	protected void registerNetValues() {
		this.radius = this.addNetValue(new IntNetValue());
		this.maxRadius = this.addNetValue(new IntNetValue(Config.SERVER.maxRadiusPerAreaStabilizer.get()));
	}

	@Override
	protected void updateNetValues(AreaStabilizerTileEntity tileEntity) {
		this.radius.set(tileEntity.getRadius());
	}
}
