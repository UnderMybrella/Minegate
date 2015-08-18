package org.abimon.mods.minecraft.minegate.items;

import java.util.List;

import org.abimon.mods.minecraft.minegate.blocks.BlockNaquityProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockMeta extends ItemBlockWithMetadata {

	public ItemBlockMeta(Block block) {
		super(block, block);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		String tooltip = StatCollector.translateToLocal(item.getUnlocalizedName() + "." + item.getItemDamage() + ".tooltip");
		if(!tooltip.equals(item.getUnlocalizedName() + "." + item.getItemDamage() + ".tooltip"))
			list.add(tooltip);
	}


}
