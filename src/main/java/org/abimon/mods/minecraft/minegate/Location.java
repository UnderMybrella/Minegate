package org.abimon.mods.minecraft.minegate;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Location 
{
	public World worldObj;
	public int x, y, z;
	
	public Location(int x, int y, int z, World worldObj){
		this.x = x;
		this.y = y;
		this.z = z;
		this.worldObj = worldObj;
	}
	
	public Location(World worldObj, int x, int y, int z){
		this(x, y, z, worldObj);
	}
	
	public Location(TileEntity tile)
	{
		this(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj());
	}
	
	public Location(int x, int y, int z)
	{
		this(x, y, z, null);
	}
	
	public Location(float x, float y, float z)
	{
		this((int) x, (int) y, (int) z, null);
	}
	
	public String toString(){
		return "Location[" + x + ", " + y + ", " + z + ", " + (worldObj != null ? (worldObj.provider.dimensionId) : "null") + "]";
	}
	
	public Block getBlock(World obj){
		return obj.getBlock(x, y, z);
	}
	
	public Block getBlock(){
		if(worldObj != null)
			return getBlock(worldObj);
		else
			return null;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		if(!(obj instanceof Location))
			return false;
		
		Location other = (Location) obj;
		
		if(other.x == x)
			if(other.y == y)
				if(other.z == z)
					if(worldObj == null || other.worldObj == null)
						return true;
					else if(worldObj.provider.dimensionId == other.worldObj.provider.dimensionId)
						return true;
		return false;
	}
}
