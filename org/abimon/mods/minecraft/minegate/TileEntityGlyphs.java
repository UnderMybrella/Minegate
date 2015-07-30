package org.abimon.mods.minecraft.minegate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGlyphs extends TileEntity 
{
	String glyphName;
	
	public void readFromNBT(NBTTagCompound nbt)
    {
		super.readFromNBT(nbt);
		glyphName = nbt.getString("GlyphName");
    }

    public void writeToNBT(NBTTagCompound nbt){
    	super.writeToNBT(nbt);
    	nbt.setString("GlyphName", glyphName);
    }
}
