package org.abimon.mods.minecraft.minegate.tileentity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.MineGate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDHD extends TileEntity 
{
	public Glyph[] glyphs = new Glyph[32];
	public LinkedList<Glyph> pressedGlyphs = new LinkedList<Glyph>();
	public boolean[] pressed = new boolean[32];

	public int boundX;
	public int boundY;
	public int boundZ;

	public TileEntityDHD(){
		for(int i = 0; i < glyphs.length; i++)
			glyphs[i] = MineGate.glyphList.get(0);
		for(int i = 0; i < pressed.length; i++)
			pressed[i] = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		for(int i = 0; i < glyphs.length; i++)
		{
			int location = 0;
			for(; location < MineGate.glyphList.size(); location++)
				if(MineGate.glyphList.get(location).equals(glyphs[i]))
					break;

			nbt.setInteger("Glyph" + i, location);
		}

		for(int i = 0; i < pressed.length; i++)
			nbt.setBoolean("Pressed" + i, pressed[i]);

		for(int i = 0; i < pressedGlyphs.size(); i++)
		{
			int location = 0;
			for(; location < MineGate.glyphList.size(); location++)
				if(MineGate.glyphList.get(location).equals(pressedGlyphs.get(i)))
					break;

			nbt.setInteger("PressedGlyph" + i, location);
		}

		nbt.setInteger("BoundX", boundX);
		nbt.setInteger("BoundY", boundY);
		nbt.setInteger("BoundZ", boundZ);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);

		for(int i = 0; i < glyphs.length; i++)
			if(nbt.getInteger("Glyph" + i) == MineGate.glyphList.size())
				glyphs[i] = MineGate.glyphList.getFirst();
			else
				glyphs[i] = MineGate.glyphList.get(nbt.getInteger("Glyph" + i));

		for(int i = 0; i < pressed.length; i++)
			if(nbt.hasKey("Pressed" + i))
				pressed[i] = nbt.getBoolean("Pressed" + i);

		for(int i = 0; i < glyphs.length; i++)
			if(nbt.hasKey("PressedGlyph" + i))
				if(nbt.getInteger("PressedGlyph" + i) == MineGate.glyphList.size())
					pressedGlyphs.add(MineGate.glyphList.getFirst());
				else
					pressedGlyphs.add(MineGate.glyphList.get(nbt.getInteger("PressedGlyph" + i)));

		boundX = nbt.getInteger("BoundX");
		boundY = nbt.getInteger("BoundY");
		boundZ = nbt.getInteger("BoundZ");
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

	public static TileEntity constructMulticulturalDHD() {
		TileEntityDHD dhd = new TileEntityDHD();
		for(int i = 0; i < dhd.glyphs.length; i++)
			dhd.glyphs[i] = MineGate.glyphList.get(i);
		return dhd;
	}
}
