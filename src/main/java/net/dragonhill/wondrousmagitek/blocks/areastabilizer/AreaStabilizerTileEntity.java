package net.dragonhill.wondrousmagitek.blocks.areastabilizer;

import net.dragonhill.wondrousmagitek.config.Config;
import net.dragonhill.wondrousmagitek.global.ScopedState;
import net.dragonhill.wondrousmagitek.global.chunkLoading.AreaStabilizerManager;
import net.dragonhill.wondrousmagitek.init.ModBlocks;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class AreaStabilizerTileEntity extends TileEntity implements INamedContainerProvider {

	private String owner;
	private int radius;

	public String getOwner() {
		return this.owner;
	}

	public int getRadius() {
		return this.radius;
	}

	public void setRadius(ServerPlayerEntity sender, int radius) {
		if(!sender.isCreative() && this.owner != sender.getName().getString()) {
			LogHelper.getLogger().warn("The player {} tried to update a area stabilizer not owned by itself", sender.getName().getString());
			return;
		}

		if(radius > Config.SERVER.maxRadiusPerAreaStabilizer.get()) {
			LogHelper.getLogger().warn("The player {} to set the radius too big", sender.getName().getString());
			return;
		}

		this.radius = radius;
		AreaStabilizerManager.getInstance().addOrUpdate(this);
	}

	public AreaStabilizerTileEntity() {
		super(ModTileEntities.areaStabilizerTileEntity.get());
	}

	public void onBlockPlayedBy(LivingEntity entity) {
		this.owner = null;
		this.radius = 0;

		ServerPlayerEntity playerEntity = entity.getEntityWorld().getServer().getPlayerList().getPlayerByUUID(entity.getUniqueID());

		if(playerEntity != null) {
			this.owner = playerEntity.getName().getString();
		}

		this.updateRegistration();
		this.markDirty();
	}

	private void updateRegistration() {
		if(this.world.isRemote) {
			return;
		}
		AreaStabilizerManager.getInstance().addOrUpdate(this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return ModBlocks.areaStabilizer.get().getNameTextComponent();
	}

	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new AreaStabilizerContainer(windowId, inventory, this);
	}

	@Override
	public void onLoad() {
		super.onLoad();

		if(this.world.isRemote) {
			return;
		}

		this.updateRegistration();
	}

	@Override
	public void remove() {
		super.remove();

		if(this.world.isRemote) {
			if(ScopedState.areaStabilizerVisualization != null) {
				ChunkPos self = new ChunkPos(this.getPos());
				if(self.equals(ScopedState.areaStabilizerVisualization.getA())) {
					ScopedState.areaStabilizerVisualization = null;
				}
			}
			return;
		}

		AreaStabilizerManager.getInstance().remove(this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		compound.putString("owner", this.owner);
		compound.putInt("radius", this.radius);

		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);

		this.owner = compound.getString("owner");
		if(this.owner.isEmpty()) {
			this.owner = null;
		}

		this.radius = compound.getInt("radius");
		if(this.radius < 0) {
			this.radius = 0;
		}
	}
}
