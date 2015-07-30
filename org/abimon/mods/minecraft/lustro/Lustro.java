package org.abimon.mods.minecraft.lustro;

import java.io.File;
import java.io.PrintStream;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.potion.Potion;

@Mod(modid = Lustro.modid, version = Lustro.version)
public class Lustro 
{
	public static final String modid = "Lustro";
	public static final String version = "Uno";

	@EventHandler
	public void postinit(FMLPostInitializationEvent event){
		File file = new File("Diagnostics");
		if(!file.exists())
			file.mkdir();

		try{
			File potions = new File(file.getAbsolutePath() + File.separator + "Potions.txt");
			if(!potions.exists())
				potions.createNewFile();
			PrintStream potionOut = new PrintStream(potions);
			for(int i = 0; i < Potion.potionTypes.length; i++)
				if(Potion.potionTypes[i] == null)
					potionOut.println(i + ": null");
				else
					potionOut.println(i + ": " + Potion.potionTypes[i].getClass() + ", " + Potion.potionTypes[i].getName());
		}
		catch(Throwable th){
			System.err.println("Potions error: " + th);
		}
	}
}
