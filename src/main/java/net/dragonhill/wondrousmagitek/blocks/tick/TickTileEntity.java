package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.init.ModBlocks;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;

public class TickTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	private String name = "";

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		this.markDirty();
	}

	public TickTileEntity() {
		super(ModTileEntities.tickTileEntity.get());
	}

	public String getLocationCompact() {
		return String.format("%s(%d, %d, %d)", DimensionType.getKey(this.world.dimension.getType()), pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void tick() {
		if(this.world.getGameTime() % 20 == 0 && this.world.isRemote() == false) {
			LogHelper.getLogger().info(String.format("TickBlock[%s]", this.name.isEmpty() ? this.getLocationCompact() : this.name));
		}
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(ModBlocks.tickBlock.get().getTranslationKey());
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new TickContainer(windowId, inventory, this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		compound.putString("name", this.name);

		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);

		this.name = compound.getString("name");
	}
}
