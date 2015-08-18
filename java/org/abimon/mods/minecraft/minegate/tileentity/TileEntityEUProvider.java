package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.naquity.NaquityValues;

import cpw.mods.fml.common.FMLCommonHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.info.Info;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityEUProvider extends TileEntityNaquityProvider implements IEnergySink{

	private boolean addedToEnet;

	public TileEntityEUProvider(){
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity arg0, ForgeDirection arg1) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		return 65536;
	}

	@Override
	public int getSinkTier() {
		return 4;
	}

	@Override
	public double injectEnergy(ForgeDirection arg0, double energy, double voltage) {
		this.naquity += energy / NaquityValues.EU;
		return 0;
	}

	public void updateEntity()
	{
		if ((!addedToEnet) && (!FMLCommonHandler.instance().getEffectiveSide().isClient()) && (Info.isIc2Available()))
		{

			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			addedToEnet = true;
		}
	}

	public void onChunkUnload()
	{
		if ((addedToEnet) && (Info.isIc2Available()))
		{
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			addedToEnet = false;
		}
	}

}
