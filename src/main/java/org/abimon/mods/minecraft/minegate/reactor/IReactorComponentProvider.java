package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.item.ItemStack;

public interface IReactorComponentProvider {
	public NaquadahReactorComponent getComponent(ItemStack item, TileEntityNaquadahReactor reactor);
}
