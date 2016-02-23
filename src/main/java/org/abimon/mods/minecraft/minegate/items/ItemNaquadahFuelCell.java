package org.abimon.mods.minecraft.minegate.items;

import org.abimon.mods.minecraft.minegate.reactor.IReactorComponentProvider;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahFuelCell;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahReactorComponent;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNaquadahFuelCell extends Item implements IReactorComponentProvider {

	
	
	@Override
	public NaquadahReactorComponent getComponent(ItemStack item, TileEntityNaquadahReactor reactor) {
		return new NaquadahFuelCell(reactor,(Short.MAX_VALUE - this.getDamage(item)) / 10922);
	}

}
