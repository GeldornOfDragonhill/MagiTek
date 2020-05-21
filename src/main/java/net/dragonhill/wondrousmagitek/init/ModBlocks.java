package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerBlock;
import net.dragonhill.wondrousmagitek.blocks.BlockBase;
import net.dragonhill.wondrousmagitek.blocks.tick.TickBlock;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
	public static final DeferredRegister<Block> blocks = new DeferredRegister<>(ForgeRegistries.BLOCKS, Constants.modId);

	private static <T extends BlockBase> RegistryObject<T> register(String name, Supplier<T> factory, boolean hasBlockItem) {
		RegistryObject<T> blockRegistryObject = blocks.register(name, factory);

		if(hasBlockItem) {
			ModItems.items.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().group(ModItemGroups.mainItemGroup)));
		}

		return blockRegistryObject;
	}

	public static RegistryObject<AreaStabilizerBlock> areaStabilizer = register("area_stabilizer", AreaStabilizerBlock::new, true);
	public static RegistryObject<TickBlock> tickBlock = register("tick_block", TickBlock::new, true);
}
