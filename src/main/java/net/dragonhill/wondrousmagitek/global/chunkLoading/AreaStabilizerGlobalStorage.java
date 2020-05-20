package net.dragonhill.wondrousmagitek.global.chunkLoading;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AreaStabilizerGlobalStorage extends WorldSavedData {

	private static final String listName = "tileEntityPositions";

	private Consumer<List<DimensionBlockPosition>> dataLoadedAction;
	private final Supplier<List<DimensionBlockPosition>> positionsRetrieval;

	public void setDataLoadedAction(Consumer<List<DimensionBlockPosition>> dataLoadedAction) {
		this.dataLoadedAction = dataLoadedAction;
	}

	public AreaStabilizerGlobalStorage(String name, Consumer<List<DimensionBlockPosition>> dataLoadedAction, Supplier<List<DimensionBlockPosition>> positionsRetrieval) {
		super(name);
		this.dataLoadedAction = dataLoadedAction;
		this.positionsRetrieval = positionsRetrieval;
	}

	@Override
	public void read(CompoundNBT nbt) {
		INBT entryNbt = nbt.get(AreaStabilizerGlobalStorage.listName);

		if(entryNbt instanceof ListNBT) {
			ListNBT listNBT = (ListNBT)entryNbt;
			List<DimensionBlockPosition> positions = new ArrayList<>(listNBT.size());

			for(int i = 0; i < listNBT.size(); ++i) {
				CompoundNBT compoundNBT = listNBT.getCompound(i);
				positions.add(DimensionBlockPosition.readFromNBT(compoundNBT));
			}

			this.dataLoadedAction.accept(positions);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		List<DimensionBlockPosition> positions = this.positionsRetrieval.get();

		ListNBT listNBT = new ListNBT();

		for(DimensionBlockPosition position : positions) {
			CompoundNBT compoundNBT = new CompoundNBT();
			position.writeToNBT(compoundNBT);
			listNBT.add(compoundNBT);
		}

		compound.put(AreaStabilizerGlobalStorage.listName, listNBT);
		return compound;
	}
}
