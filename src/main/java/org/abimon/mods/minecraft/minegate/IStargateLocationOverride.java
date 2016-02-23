package org.abimon.mods.minecraft.minegate;

import net.minecraft.world.World;

/** For Tile Entities */
public interface IStargateLocationOverride {
	
	/**
	 * Location override, params are for the Controller
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Location getLocationOverride(World world, int x, int y, int z);
	
	/** Gets the priority. Default Stargate location priority is 10. Highest priority wins*/
	public int getPriority();
}
