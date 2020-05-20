package net.dragonhill.wondrousmagitek.tileentities;

import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.dragonhill.wondrousmagitek.util.LogHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TickTileEntity extends TileEntity implements ITickableTileEntity {
	public TickTileEntity() {
		super(ModTileEntities.tickTileEntity.get());
	}

	@Override
	public void tick() {
		if(this.world.getGameTime() % 10 == 0 && this.world.isRemote() == false) {
			LogHelper.getLogger().warn(String.format("Ticking at %s", this.pos.toString()));
		}
	}
}
