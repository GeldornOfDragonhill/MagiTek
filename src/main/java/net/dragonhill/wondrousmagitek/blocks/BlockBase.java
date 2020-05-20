package net.dragonhill.wondrousmagitek.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.function.Function;

public abstract class BlockBase extends Block {
	protected BlockBase(Function<Properties, Properties> propertiesInitializer, Material material) {
		super(propertiesInitializer.apply(Block.Properties.create(material)));
	}
}
