package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NaquadahFuelCell extends NaquadahReactorComponent {

	float naquadahContent = 3;

	public NaquadahFuelCell(TileEntityNaquadahReactor reactor, float naquadahContent) {
		super(reactor);
		this.naquadahContent = naquadahContent;
	}

	@Override
	public float getPowerModifier() {
		return 1;
	}

	@Override
	public float getFuelValue() {
		return naquadahContent;
	}

	@Override
	public float useFuel(float fuel) {
		if(naquadahContent >= fuel){
			naquadahContent -= fuel;
			fuel = 0;
		}
		else{
			fuel -= naquadahContent;
			naquadahContent = 0;
		}
		return fuel;
	}
	
	public ResourceLocation getResourceLocation(){
		return new ResourceLocation(MineGate.MODID + ":textures/reactor/naquadah_cell_" + (naquadahContent > 2.9 ? "full" : naquadahContent >= 2 ? "mostly_full" : naquadahContent >= 1.25 ? "half_full" : naquadahContent > 0.2 ? "mostly_empty" : "empty") + ".png");
	}

	@Override
	public void onTick() {

	}

	@Override
	public ItemStack getItemStackForComponent() {
		return naquadahContent > 0 ? new ItemStack(MineGate.naquadahFuelCell, 1,  (int) (Short.MAX_VALUE - (naquadahContent * 10922))) : null;
	}

	@Override
	public float getFuelConsumption() {
		return 1;
	}

}
