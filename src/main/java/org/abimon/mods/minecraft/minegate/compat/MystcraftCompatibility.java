package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.blocks.BlockBookHolder;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityBookHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.instability.InstabilityBlockManager;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.symbol.modifiers.SymbolBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class MystcraftCompatibility {
	
	Logger log;
	
	public static final Block bookHolder = new BlockBookHolder().setBlockName(MineGate.MODID + ":bookHolder");
	
	public static final BlockDescriptor NAQUADAH_ORE = new BlockDescriptor(MineGate.naquadahOre);
	
	public static final IAgeSymbol NAQUADAH_ORE_PAGE = new SymbolBlock(NAQUADAH_ORE, "Naquadah Ore");
	
	public MystcraftCompatibility(){
		MineGate.mystcraftLoaded = true;
		
		log = LogManager.getLogger("[MineGate x Mystcraft]");
		log.info("Registering book holder...");
		
		GameRegistry.registerBlock(bookHolder, "bookHolder");
		GameRegistry.registerTileEntity(TileEntityBookHolder.class, "bookHolder");
		
//		log.info("Registering pages...");
//		SymbolManager.registerSymbol(NAQUADAH_ORE_PAGE, true, MineGate.MODID);
//		
//		NAQUADAH_ORE.setUsable(BlockCategory.SOLID, true);
//		
//		log.info("Making things unstable...");
//		InstabilityBlockManager.setInstabilityFactors(MineGate.naquadahOre, 10000, 10000);
		
	}
}
