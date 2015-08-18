package org.abimon.mods.minecraft.minegate;

import java.util.LinkedList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GlyphSetter implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		return getCraftingResult(inv) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		
		ItemStack glyphSetter = null;
		
		LinkedList<Integer> glyphs = new LinkedList<Integer>();
		
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack item = inv.getStackInSlot(i);
			if(item == null)
				continue;
			if(item.getItem() == MineGate.glyphSetter)
				glyphSetter = item.copy();
			if(item.getItem() == MineGate.glyphs)
				glyphs.add(item.getItemDamage());
		}
		
		if(glyphSetter == null || glyphs.size() == 0)
			return null;
		
		glyphSetter.stackSize = 1;
		
		if(!glyphSetter.hasTagCompound())
			glyphSetter.setTagCompound(new NBTTagCompound());
		
		if(glyphSetter.getTagCompound().hasKey("Glyphs")){
			int[] array = glyphSetter.getTagCompound().getIntArray("Glyphs");
			int[] newArray = new int[array.length + glyphs.size()];
			for(int i = 0; i < array.length; i++)
				newArray[i] = array[i];
			for(int i = array.length; i < newArray.length; i++)
				newArray[i] = glyphs.pollFirst();
			glyphSetter.getTagCompound().setIntArray("Glyphs", newArray);
		}
		else
		{
			int[] newArray = new int[glyphs.size()];
			for(int i = 0; i < newArray.length; i++)
				newArray[i] = glyphs.pollFirst();
			glyphSetter.getTagCompound().setIntArray("Glyphs", newArray);
		}
		
		return glyphSetter;
	}

	@Override
	public int getRecipeSize() {
		return 4;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
