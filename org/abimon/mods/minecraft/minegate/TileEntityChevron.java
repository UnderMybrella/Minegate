package org.abimon.mods.minecraft.minegate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityChevron extends TileEntity 
{
	Glyph glyph;
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		glyph = MineGate.glyphList.get(nbt.getInteger("Glyph"));
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		for(int i = 0; i < MineGate.glyphList.size(); i++)
			if(MineGate.glyphList.get(i).equals(glyph))
			{
				nbt.setInteger("Glyph", i);
				break;
			}
	}
}
