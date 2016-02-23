package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class ReactorConverter extends NaquadahReactorComponent {

	EnumEnergy energy;
	int storedEnergy = 0;

	public ReactorConverter(EnumEnergy energy, TileEntityNaquadahReactor reactor) {
		super(reactor);
		
		if(energy == null)
			energy = EnumEnergy.NAQUITY;
		this.energy = energy;
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
		
		if((storedEnergy + (energy.getConversionRate() * reactor.naquity)) < Integer.MAX_VALUE){
			storedEnergy += energy.getConversionRate() * reactor.naquity;
			reactor.naquity = 0;
		}
	}

	public float getEnergyStored(EnumEnergy energyForm){
		if(energyForm == energy)
			return storedEnergy;
		return 0;
	}
	
	public float retrieveEnergy(EnumEnergy energyForm, float request){
		if(energyForm == energy){
			if(request <= storedEnergy){
				storedEnergy -= request;
				return request;
			}
			else{
				int stored = storedEnergy;
				storedEnergy = 0;
				return stored;
			}
		}
		return 0;
	}

	@Override
	public ItemStack getItemStackForComponent() {
		return new ItemStack(MineGate.reactorComponents, 1, 5);
	}
	
	public ResourceLocation getResourceLocation(){
		return new ResourceLocation(MineGate.MODID + ":textures/reactor/reactor_converter_" + energy.name().toLowerCase() + ".png");
	}

}
