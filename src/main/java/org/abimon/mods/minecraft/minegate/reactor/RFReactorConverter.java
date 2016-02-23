package org.abimon.mods.minecraft.minegate.reactor;

import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class RFReactorConverter extends ReactorConverter {
	
	public RFReactorConverter(TileEntityNaquadahReactor reactor) {
		super(EnumEnergy.RF, reactor);
	}

	public void onTick(){
		super.onTick();
		
		int counter = 0;
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
			TileEntity te = reactor.getWorldObj().getTileEntity(direction.offsetX + reactor.xCoord, direction.offsetY + reactor.yCoord, direction.offsetZ + reactor.zCoord);
			if(te instanceof IEnergyReceiver){
				IEnergyReceiver ier = (IEnergyReceiver) te;
				int attemptedAmount = storedEnergy / (ForgeDirection.VALID_DIRECTIONS.length - counter++); //I support fourth-dimensional rights
				if(ier.canConnectEnergy(direction.getOpposite())){
					int actualAmount = ier.receiveEnergy(direction.getOpposite(), attemptedAmount, false);
					storedEnergy -= actualAmount;
				}
			}	
		}
	}
}
