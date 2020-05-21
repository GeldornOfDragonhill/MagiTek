package net.dragonhill.wondrousmagitek.blocks.areastabilizer;

import net.dragonhill.wondrousmagitek.blocks.BlockBase;
import net.dragonhill.wondrousmagitek.config.Config;
import net.dragonhill.wondrousmagitek.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AreaStabilizerBlock extends BlockBase {
	public AreaStabilizerBlock() {
		super(props -> props.hardnessAndResistance(3f), Material.IRON);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		if(!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof AreaStabilizerTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, buffer -> {
					buffer.writeBlockPos(pos); //TODO: refactor to some helper has the first parameter is always needed
				});
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.FAIL;
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
