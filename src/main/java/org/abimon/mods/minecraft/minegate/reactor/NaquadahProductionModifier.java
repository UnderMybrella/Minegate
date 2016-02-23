package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NaquadahProductionModifier extends NaquadahReactorComponent {

	int mk = 1;
	
	public NaquadahProductionModifier(TileEntityNaquadahReactor reactor, int make) {
		super(reactor);
		this.mk = make;
	}

	@Override
	public float getPowerModifier() {
		return mk + 1;
	}

	@Override
	public float getFuelValue() {
		return 0;
	}

	@Override
	public float getFuelConsumption() {
		return ((float) Math.pow(2, mk) - 1);
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
		return new ItemStack(MineGate.reactorComponents, 1, 0 + mk);
	}
	
	public ResourceLocation getResourceLocation(){
		return new ResourceLocation(MineGate.MODID + ":textures/reactor/power_reactor_component_mk_" + mk + ".png");
	}

}
