package org.abimon.mods.minecraft.minegate.tileentity;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.Location;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHyperDrive extends TileEntity 
{
	public LinkedList<Location> transferLocations = new LinkedList<Location>();

	Location cs = null;

	Block original = null;

	public int index = 0;

	public void updateEntity(){
		if(index != -1)
			return;
		if(cs == null)
		{
			cs = new Location(xCoord, yCoord, zCoord);
			transferLocations.clear();
			transferLocations.add(cs);
			Location north = new Location(cs.x, cs.y, cs.z-1, worldObj);
			Location south = new Location(cs.x, cs.y, cs.z+1, worldObj);
			Location east = new Location(cs.x+1, cs.y, cs.z, worldObj);
			Location west = new Location(cs.x-1, cs.y, cs.z, worldObj);
			Location up = new Location(cs.x, cs.y+1, cs.z, worldObj);
			Location down = new Location(cs.x, cs.y-1, cs.z, worldObj);

			for(Location loc : new Location[]{north, south, east, west, up, down})
				if(!transferLocations.contains(loc) && (loc.getBlock() != null && loc.getBlock() != Blocks.air))
				{	
					transferLocations.add(loc);
					if(worldObj.getBlock(loc.x, loc.y+32, loc.z+64) == Blocks.air)
					{
						if(original == null)
							original = loc.getBlock();

						if(original != loc.getBlock())
							continue;
						worldObj.setBlock(loc.x, loc.y+32, loc.z+64, loc.getBlock());
						worldObj.setBlock(loc.x, loc.y, loc.z, Blocks.air);
					}
				}

			index = transferLocations.indexOf(cs);
			if(index >= transferLocations.size() - 1)
			{
				index = -1;
			}
			else
				cs = transferLocations.get(index+1);
		}
	}

	public boolean canUpdate()
	{
		return index >= 0;
	}
}
