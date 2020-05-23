package net.dragonhill.wondrousmagitek.blocks.tick;

import net.dragonhill.wondrousmagitek.blocks.BlockBase;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TickBlock extends BlockBase {
	public TickBlock() {
		super(props -> props.hardnessAndResistance(3f), Material.ANVIL);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		if(!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof TickTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, buffer -> {
					buffer.writeBlockPos(pos); //TODO: refactor to some helper has the first parameter is always needed
				});
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.FAIL;
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
