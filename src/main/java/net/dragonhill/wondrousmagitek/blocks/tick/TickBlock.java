package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.blocks.BlockBase;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TickBlock extends BlockBase {
	public TickBlock() {
		super(props -> props.hardnessAndResistance(3f), Material.ANVIL);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.tickTileEntity.get().create();
	}
}
