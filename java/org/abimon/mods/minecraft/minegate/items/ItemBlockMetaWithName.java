package org.abimon.mods.minecraft.minegate.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockMetaWithName extends ItemBlockMeta{

	public ItemBlockMetaWithName(Block block) {
		super(block);
	}
	
    public String getUnlocalizedName(ItemStack item)
    {
        return this.field_150939_a.getUnlocalizedName() + "." + item.getItemDamage();
    }

}
