package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.blocks.BlockBase;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.dragonhill.wondrousmagitek.tileentities.AreaStabilizerTileEntity;
import net.dragonhill.wondrousmagitek.tileentities.TickTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntities {
	public static final DeferredRegister<TileEntityType<?>> tileEntities = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Constants.modId);

	private static <T extends TileEntity, TBlock extends BlockBase> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factoryIn, RegistryObject<TBlock> blockRegistryObject) {
		return tileEntities.register(name, () -> TileEntityType.Builder.create(factoryIn, blockRegistryObject.get()).build(null));
	}

	public static final RegistryObject<TileEntityType<AreaStabilizerTileEntity>> areaStabilizerTileEntity = register("area_stabilizer_te", AreaStabilizerTileEntity::new, ModBlocks.areaStabilizer);
	public static final RegistryObject<TileEntityType<TickTileEntity>> tickTileEntity = register("tick_te", TickTileEntity::new, ModBlocks.tickBlock);
}
