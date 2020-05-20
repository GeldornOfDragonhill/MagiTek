package net.dragonhill.wondrousmagitek.tileentities;

import net.dragonhill.wondrousmagitek.global.chunkLoading.AreaStabilizerManager;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class AreaStabilizerTileEntity extends TileEntity {

	private String owner;
	private int radius;

	public String getOwner() {
		return this.owner;
	}

	public int getRadius() {
		return this.radius;
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
