package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityEssentiaNaquity;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityManaNaquity;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityVisNaquity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.registry.GameRegistry;

public class ThaumcraftCompatibility {
	Logger log;
	
	public static boolean loaded = false;
	
	public ThaumcraftCompatibility(){
		
		loaded = true;
		
		log = LogManager.getLogger("[MineGate x Thaumcraft]");
		
		log.info("Registering Essentia and Vis Naquity Providers");
		
		MineGate.naquityProvider.registerNewType(11, TileEntityEssentiaNaquity.class, MineGate.MODID + ":naquityProviderEssentia");
		MineGate.naquityProvider.registerNewType(12, TileEntityVisNaquity.class, MineGate.MODID + ":naquityProviderVis");
		
		GameRegistry.registerTileEntity(TileEntityEssentiaNaquity.class, "essentiaNaquity");
		GameRegistry.registerTileEntity(TileEntityVisNaquity.class, "visNaquity");
	}
}
