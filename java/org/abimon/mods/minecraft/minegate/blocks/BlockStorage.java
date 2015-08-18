package org.abimon.mods.minecraft.minegate.blocks;

import java.awt.Color;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;


/** Pure Naquadah, Refined Naquadah, Raw Naquadah, Naquadah Shard*/
public class BlockStorage extends Block {

	private IIcon[] icons = new IIcon[4];
	
	public BlockStorage() {
		super(Material.rock);
	}
	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
    	for(int i = 0; i < icons.length; i++)
    		icons[i] = register.registerIcon(this.textureName + "_" + i);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int meta)
    {
        return icons[meta];
    }
    
   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
   {
	   for(int i = 0; i < 4; i++)
       p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
   }

}
