package net.dragonhill.wondrousmagitek.init;

import net.dragonhill.wondrousmagitek.blocks.areastabilizer.AreaStabilizerContainer;
import net.dragonhill.wondrousmagitek.config.Constants;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Constants.modId);

	public static final RegistryObject<ContainerType<AreaStabilizerContainer>> AREA_STABILIZER_CONTAINER = CONTAINERS.register("area_stabilizer_container", () -> IForgeContainerType.create(AreaStabilizerContainer::new));
}
