package org.abimon.mods.minecraft.minegate.naquity;

/** Defines the constant conversion rate between x and Naquity. Divide the unit by the constant for the appropriate Naquity value 
 *  Base values were determined by using basic smelting operations, so RF used a furnace generator and MANA used the Endoflame and EU used the standard generator
 *  VIS was harder to determine
 */
public class NaquityValues 
{
	public static final float RF = 40000;
	public static final float EU = 8000;
	public static final float MANA = 24000;
	public static final float VIS = 0; //Any primal vis
	
	/** And now, the weather */
	public static final float HAND = 262144;
	public static final float FIRE = 131072;
	public static final float LAVA = 0;
	public static final float GLOWSTONE = 1000;
}
