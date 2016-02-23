package org.abimon.mods.minecraft.minegate.naquity;

/** Defines the constant conversion rate between x and Naquity. Divide the unit by the constant for the appropriate Naquity value 
*  Base values were determined by using basic smelting operations, so RF used a furnace generator and MANA used the Endoflame and EU used the standard generator
*  VIS and ESSENTIA were determined by what was deemed appropriate values (Basically trial and error)
*/
public enum EnumEnergy {
	RF(40000),
	EU(8000),
	MANA(24000),
	VIS(65536),
	ESSENTIA(512),
	
	HAND(262144),
	FIRE(131072),
	LAVA(1),
	GLOWSTONE(10000),
	NAQUITY(1);
	
	float conversion = 1.0f;
	
	private EnumEnergy(float conversion){
		this.conversion = conversion;
	}
	
	public float getConversionRate() {
		return conversion;
	}
}
