package org.abimon.mods.minecraft.minegate.compat;

import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class NLexiconEntry extends LexiconEntry {

	public NLexiconEntry(String unlocalizedName, LexiconCategory category) {
		super(unlocalizedName, category);
	}

	public String getTagline()
	{
		return unlocalizedName + ".tagline";
	}
}
