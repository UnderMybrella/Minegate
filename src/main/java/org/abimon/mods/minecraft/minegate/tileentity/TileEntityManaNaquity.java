package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;

import vazkii.botania.api.mana.IManaReceiver;

public class TileEntityManaNaquity extends TileEntityNaquityProvider implements IManaReceiver
{
	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void recieveMana(int mana) {
		naquity += ((float) mana) / EnumEnergy.MANA.getConversionRate();
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
