package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;
import org.abimon.mods.minecraft.minegate.naquity.NaquityValues;

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
		//System.out.println("Before: " + naquity);
		naquity += ((float) mana) / NaquityValues.MANA;
		//System.out.println("After: " + naquity);
		//System.out.println("The Specs: " + ((float) mana) / NaquityValues.MANA);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

}
