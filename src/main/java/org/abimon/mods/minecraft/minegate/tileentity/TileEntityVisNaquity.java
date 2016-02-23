package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileVisRelay;

public class TileEntityVisNaquity extends TileVisRelay implements INaquityProvider{

	float naquity = 0.0f;
	
	@Override
	public float naquityContent() {
		return naquity;
	}

	@Override
	public float retrieveNaquity(float removalService) {
		float difference = naquity - removalService;
		if(difference < 0)
		{
			float tmp = naquity;
			naquity = 0;
			return tmp;
		}
		naquity -= removalService;
		return removalService;
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		
		for(Aspect aspect : Aspect.getPrimalAspects()){
			float num = super.consumeVis(aspect, Math.round(EnumEnergy.VIS.getConversionRate()));
			naquity += num / EnumEnergy.VIS.getConversionRate();
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		
		nbt.setFloat("Naquity", naquity);
	}

}
