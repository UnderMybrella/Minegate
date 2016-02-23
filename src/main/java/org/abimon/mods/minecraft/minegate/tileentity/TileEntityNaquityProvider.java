package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityNaquityProvider extends TileEntity implements INaquityProvider{

	public float naquity = 0.0f;
	
	@Override
	public float naquityContent() {
		return naquity;
	}

	@Override
	public float retrieveNaquity(float removalService) {
		float difference = naquity - removalService;
		if(difference < 0)
		{
			float tmp = naquity;
			naquity = 0;
			return tmp;
		}
		naquity -= removalService;
		return removalService;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setFloat("Naquity", naquity);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		naquity = nbt.getFloat("Naquity");
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

}
