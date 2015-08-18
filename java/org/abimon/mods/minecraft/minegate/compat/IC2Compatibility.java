package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityEUProvider;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityManaNaquity;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityRFProvider;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;

public class IC2Compatibility {
	
	public IC2Compatibility() {
		MineGate.naquityProvider.registerNewType(10, TileEntityEUProvider.class, MineGate.MODID + ":naquityProviderEU");
		GameRegistry.registerTileEntity(TileEntityEUProvider.class, "euNaquity");
		GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 10), new Object[]{"SIS", "CFC", "SIS", 'S', Blocks.stone, 'I', Ic2Items.casingiron, 'C', Ic2Items.advancedCircuit, 'F', MineGate.stargateFrame});
	}
}
