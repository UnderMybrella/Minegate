package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class NaquadahReactorComponent {

	protected TileEntityNaquadahReactor reactor;

	public NaquadahReactorComponent(TileEntityNaquadahReactor reactor){
		this.reactor = reactor;
	}

	/** Multiplying the Naquity output every tick */
	public abstract float getPowerModifier();
	/** How much fuel this component provides. 0.0001 naquadah is consumed per tick */
	public abstract float getFuelValue();
	/** How much fuel this component consumes per tick. 0.0001 naquadah is consumed at base */
	public abstract float getFuelConsumption();
	/** Return leftover fuel consumption*/
	public float useFuel(float fuel){
		return fuel;
	}
	public abstract void onTick();

	public void writeToNBT(NBTTagCompound nbt){

	}

	public void readFromNBT(NBTTagCompound nbt){

	}

	public abstract ItemStack getItemStackForComponent();

	public ResourceLocation getResourceLocation(){
		return new ResourceLocation(MineGate.MODID + ":textures/reactor/blank_reactor_component.png");
	}

	public float getEnergyStored(EnumEnergy energyForm){
		return 0.0f;
	}

	/** Return how much energy was actually retrieved */
	public float retrieveEnergy(EnumEnergy energyForm, float request){
		return 0.0f;
	}
}
