package net.dragonhill.wondrousmagitek.items;

import net.minecraft.item.Item;

import java.util.function.Function;

public abstract class ItemBase extends Item {
	protected ItemBase(Function<Properties, Properties> propertiesInitializer) {
		super(propertiesInitializer.apply(new Properties()));
	}
}
