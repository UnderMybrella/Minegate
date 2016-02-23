package org.abimon.mods.minecraft.minegate.items;

import java.util.List;

import org.abimon.mods.minecraft.minegate.MineGate;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemNaquadahDerivatives extends Item 
{	
	IIcon[] icons = new IIcon[4];
	
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return icons[damage];
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
    	for(int i = 0; i < icons.length; i++)
    		icons[i] = register.registerIcon(MineGate.MODID + ":naquadah_" + i);
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
        return "item." + MineGate.MODID + ":naquadahDerivatives." + stack.getItemDamage();
    }
}
