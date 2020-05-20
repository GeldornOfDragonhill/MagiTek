package net.dragonhill.wondrousmagitek.blocks;

import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.dragonhill.wondrousmagitek.tileentities.AreaStabilizerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AreaStabilizer extends BlockBase {
	public AreaStabilizer() {
		super(props -> props.hardnessAndResistance(3f), Material.IRON);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if(worldIn.isRemote) {
			return;
		}

		AreaStabilizerTileEntity tileEntity = (AreaStabilizerTileEntity)worldIn.getTileEntity(pos);
		tileEntity.onBlockPlayedBy(placer);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.areaStabilizerTileEntity.get().create();
	}


}
