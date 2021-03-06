package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityManaNaquity;
import java.util.logging.Logger;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.page.PageEntity;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageText;

public class BotaniaCompatibility 
{
	
	Logger log;
	
	public static final String MODID = MineGate.MODID + ":botania.";
	public static LexiconCategory minegate;
	
	public static LexiconEntry naquadah;
	public static LexiconEntry stargate;
	public static LexiconEntry dialling;
	
	public static Multiblock controllerMulti;
	public static Multiblock frameMulti;
	
	public BotaniaCompatibility(){
		log = Logger.getLogger("[MineGate x Botania]");
		log.info("Botania is loaded. Loading lexica entries...");
		
		minegate = new LexiconCategory(MODID + "category");
		minegate.setIcon(new ResourceLocation(MineGate.MODID + ":glyphs/earth.png"));
		
		naquadah = new LexiconEntry(MODID + "naquadah", minegate).setLexiconPages(new PageText(MODID + "naquadah.one"));
		naquadah.setIcon(new ItemStack(MineGate.naquadahDerivatives));
		
		controllerMulti = new Multiblock();
		controllerMulti.addComponent(0, 1, 0, MineGate.stargateController, 0);
		
		frameMulti = new Multiblock();
		frameMulti.addComponent(0, 1, 0, MineGate.stargateFrame, 0);
		
		stargate = new LexiconEntry(MODID + "stargate", minegate).setLexiconPages(new PageText(MODID + "stargate.general"), new PageText(MODID + "stargate.general.1"), new PageText(MODID + "stargate.controller"), new PageText(MODID + "stargate.controller.1"), new PageText(MODID + "stargate.controller.2"), new PageMultiblock(MODID + "stargate.controller.desc", new MultiblockSet(controllerMulti)), new PageText(MODID + "stargate.frame"), new PageMultiblock(MODID + "stargate.frame.desc", new MultiblockSet(frameMulti)), new PageText(MODID + "stargate.naquity"), new PageText(MODID + "stargate.dialling"));
		stargate.setIcon(new ItemStack(MineGate.stargateController));
		dialling = new LexiconEntry(MODID + "dialling", minegate).setLexiconPages(new PageTileEntity(MODID + "dialling.dhd", TileEntityDHD.constructMulticulturalDHD(), 1, 2, 80));
		dialling.setIcon(new ItemStack(MineGate.dialHomeDevice));
		
		BotaniaAPI.addCategory(minegate);
		
		BotaniaAPI.addEntry(naquadah, minegate);
		BotaniaAPI.addEntry(stargate, minegate);
		BotaniaAPI.addEntry(dialling, minegate);
		
		log.info("Adding mana provider...");
		MineGate.naquityProvider.registerNewType(8, TileEntityManaNaquity.class, MineGate.MODID + ":naquityProviderMana");
		GameRegistry.registerTileEntity(TileEntityManaNaquity.class, "manaNaquity");
		
		GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 8), new Object[]{"SAS", "IFI", "SIS", 'S', Blocks.stone, 'I', ModBlocks.storage, 'F', MineGate.stargateFrame, 'A', ModBlocks.spreader});
	
		BotaniaAPI.registerManaInfusionRecipe(new ItemStack(MineGate.naquadahDerivatives, 1, 0), new ItemStack(MineGate.naquadahDerivatives, 1, 3), 999999); //Pls don't kill me .-.
	}
}
