package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	public static final DeferredRegister<Item> items = new DeferredRegister<>(ForgeRegistries.ITEMS, Constants.modId);
}
