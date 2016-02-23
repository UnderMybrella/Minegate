package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileJarFillable;

public class TileEntityEssentiaNaquity extends TileJarFillable implements INaquityProvider{

	public float naquity = 0.0f;
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		naquity += this.amount / EnumEnergy.ESSENTIA.getConversionRate();
		
		this.amount = 0;
		this.aspect = null;
	}

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

	public int getMinimumSuction()
	{
		return 999;
	}

	public int getSuctionAmount(ForgeDirection loc)
	{
		return 999;
	}


}
