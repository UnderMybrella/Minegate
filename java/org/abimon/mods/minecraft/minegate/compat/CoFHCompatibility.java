package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityManaNaquity;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityRFProvider;

import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.simple.BlockFrame;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalfoundation.block.TFBlocks;
import cofh.thermalfoundation.item.TFItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CoFHCompatibility {
	public CoFHCompatibility() {
		MineGate.naquityProvider.registerNewType(9, TileEntityRFProvider.class, MineGate.MODID + ":naquityProviderRF");
		GameRegistry.registerTileEntity(TileEntityRFProvider.class, "rfNaquity");
		if(MineGate.doesClassExist("cofh.thermalexpansion.block.TEBlocks"))
			for(ItemStack frame : new ItemStack[]{BlockFrame.frameMachineBasic, BlockFrame.frameMachineHardened, BlockFrame.frameMachineReinforced, BlockFrame.frameMachineResonant})
				GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 9), new Object[]{"SMS", "GFG", "SCS", 'S', Blocks.stone, 'M', frame, 'F', MineGate.stargateFrame, 'G', TEBlocks.blockGlass, 'C', TEItems.powerCoilGold});
		if(MineGate.doesClassExist("cofh.thermalfoundation.item.TFItems"))
			GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 9), new Object[]{"STS", "GFG", "SCS", 'S', Blocks.stone, 'T', TFItems.dustBasalz, 'F', MineGate.stargateFrame, 'G', TFItems.gearElectrum, 'C', TFItems.gearSignalum});
		GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 9), new Object[]{"SRS", "RFR", "SRS", 'S', Blocks.stone, 'F', MineGate.stargateFrame, 'R', Blocks.redstone_block});
	}

}
