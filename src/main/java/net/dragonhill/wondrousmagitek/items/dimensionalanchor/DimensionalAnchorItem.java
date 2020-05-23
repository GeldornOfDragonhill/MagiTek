package net.dragonhill.wondrousmagitek.items.dimensionalanchor;

import net.dragonhill.wondrousmagitek.init.ModItemGroups;
import net.dragonhill.wondrousmagitek.items.ItemBase;

public class DimensionalAnchorItem extends ItemBase {
	public DimensionalAnchorItem() {
		super(prop -> prop.maxStackSize(1).group(ModItemGroups.mainItemGroup));
	}
}
