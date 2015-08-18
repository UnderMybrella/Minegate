package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.NaquityValues;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRFProvider extends TileEntityNaquityProvider implements IEnergyReceiver
{

	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int num, boolean sim) {
		if(!sim)
			this.naquity += num / NaquityValues.RF;
		return num;
	}

}
