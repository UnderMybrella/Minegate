package org.abimon.mods.minecraft.minegate;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageText;

public class BotaniaCompatability 
{
	
	public static final String MODID = MineGate.MODID + ":botania.";
	public static LexiconCategory minegate;
	
	public static LexiconEntry naquadah;
	public static LexiconEntry stargate;
	
	public BotaniaCompatability(){
		System.out.println("Botania is loaded. Loading lexica entries...");
		
		minegate = new LexiconCategory(MODID + "category");
		
		naquadah = new LexiconEntry(MODID + "naquadah", minegate).setLexiconPages(new PageText(MODID + "naquadah.one"));
		
		Multiblock controllerMulti = new Multiblock();
		controllerMulti.addComponent(0, 1, 0, MineGate.stargateController, 0);
		
		stargate = new LexiconEntry(MODID + "stargate", minegate).setLexiconPages(new PageText(MODID + "stargate.general"), new PageText(MODID + "stargate.controller"), new PageMultiblock(MODID + "stargate.controller.desc", new MultiblockSet(controllerMulti)));
		
		BotaniaAPI.addCategory(minegate);
		
		BotaniaAPI.addEntry(naquadah, minegate);
		BotaniaAPI.addEntry(stargate, minegate);
	}
}
