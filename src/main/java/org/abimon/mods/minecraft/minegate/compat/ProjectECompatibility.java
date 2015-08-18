package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;

import cpw.mods.fml.common.registry.GameRegistry;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class ProjectECompatibility 
{
	public ProjectECompatibility(){
		ProjectEAPI.registerCustomEMC(new ItemStack(MineGate.naquadahDerivatives, 1, 3), 524088);
		ProjectEAPI.registerCustomEMC(new ItemStack(MineGate.naquadahDerivatives, 1, 0), 0);
		
		//You don't get the cheap recipe :P
		
		for(int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); i++)
		{
			ItemStack item = ((IRecipe) CraftingManager.getInstance().getRecipeList().get(i)).getRecipeOutput();
			if(item != null && item.getItem() == MineGate.naquadahDerivatives && item.getItemDamage() == 1)
				CraftingManager.getInstance().getRecipeList().remove(i);
		}
		
		//Readd the 'harder' recipe
		GameRegistry.addRecipe(new ItemStack(MineGate.naquadahDerivatives, 1, 1), new Object[]{"NNN", "NIN", "NNN", 'N', new ItemStack(MineGate.naquadahDerivatives, 1, 3), 'I', Blocks.iron_block});
	}
}
