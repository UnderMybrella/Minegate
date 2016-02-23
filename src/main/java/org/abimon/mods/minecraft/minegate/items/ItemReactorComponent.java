package org.abimon.mods.minecraft.minegate.items;

import java.util.List;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.reactor.IReactorComponentProvider;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahProductionModifier;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahReactorCasing;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahReactorComponent;
import org.abimon.mods.minecraft.minegate.reactor.RFReactorConverter;
import org.abimon.mods.minecraft.minegate.reactor.ReactorConverter;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemReactorComponent extends Item implements IReactorComponentProvider{
	IIcon[] icons = new IIcon[6];

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage)
	{
		return icons[damage];
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		for(int i = 0; i < icons.length; i++)
			icons[i] = register.registerIcon(MineGate.MODID + ":reactor_component_" + i);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs p_150895_2_, List list)
	{
		for(int i = 0; i < icons.length; i++)
			if(icons[i] != null)
				list.add(new ItemStack(item, 1, i));
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + MineGate.MODID + ":reactorComponent." + stack.getItemDamage();
	}

	@Override
	public NaquadahReactorComponent getComponent(ItemStack item, TileEntityNaquadahReactor reactor) {
		if(item.getItemDamage() > 0 && item.getItemDamage() < 5)
			return new NaquadahProductionModifier(reactor, item.getItemDamage());
		switch(item.getItemDamage()){
		case 5:
			return new RFReactorConverter(reactor);
		default:
			return new NaquadahReactorCasing(reactor);
		}
	}
}
