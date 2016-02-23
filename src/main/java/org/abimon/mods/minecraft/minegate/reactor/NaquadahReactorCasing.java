package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.ItemStack;

public class NaquadahReactorCasing extends NaquadahReactorComponent {

	public NaquadahReactorCasing(TileEntityNaquadahReactor reactor) {
		super(reactor);
	}

	@Override
	public float getPowerModifier() {
		return 1;
	}

	@Override
	public float getFuelValue() {
		return 0;
	}

	@Override
	public float getFuelConsumption() {
		return 1;
	}

	@Override
	public float useFuel(float fuel) {
		return fuel;
	}

	@Override
	public void onTick() {

	}

	@Override
	public ItemStack getItemStackForComponent() {
		return new ItemStack(MineGate.reactorComponents);
	}

}
