package org.abimon.mods.minecraft.minegate.naquity;

/** Use this for anything that can have Naquity taken out of it 
 *  Note that it is up to receivers to process everything here, up to them to take their Naquity
 *  While not required, it is HIGHLY recommended that blocks implement INaquadahBlock
 */
public interface INaquityProvider 
{
	/** How much naquity is here
	 * @return How much naquity is actually here
	 */
	public float naquityContent();
	
	/** Retrieving what is rightfully mine
	 * @param removalService How much naquity we're retrieving
	 * @return The amount of naquity actually retrieved
	 */
	public float retrieveNaquity(float removalService);
}
