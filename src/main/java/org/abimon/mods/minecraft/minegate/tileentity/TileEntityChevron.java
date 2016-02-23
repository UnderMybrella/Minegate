package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.MineGate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityChevron extends TileEntity 
{
	public Glyph glyph;
	public int glyphLocation = 0;
	public int prevPower = 0;

	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		int glyphNumber = nbt.getInteger("Glyph");
		if(glyphNumber == -1)
			glyph = null;
		else
			glyph = MineGate.glyphList.get(glyphNumber);
		glyphLocation = nbt.getInteger("GlyphLocation");
		prevPower = nbt.getInteger("PrevPower");
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		nbt.setInteger("Glyph", -1);
		for(int i = 0; i < MineGate.glyphList.size(); i++)
			if(MineGate.glyphList.get(i).equals(glyph))
			{
				nbt.setInteger("Glyph", i);
				break;
			}

		nbt.setInteger("GlyphLocation", glyphLocation);
		nbt.setInteger("PrevPower", prevPower);
	}
}
