package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {
	public static final ItemGroup mainItemGroup = new ItemGroup(Constants.modId) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.areaStabilizer.get());
		}
	};
}
