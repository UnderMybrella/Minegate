package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityControl;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class FramePeripheral implements IPeripheral {

	Location loc;
	
	public FramePeripheral(Location loc){
		this.loc = loc;
	}
	
	@Override
	public String getType() {
		return "Stargate Peripheral";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"disengage", "engage"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {

		final TileEntityControl cc = (TileEntityControl) loc.worldObj.getTileEntity(loc.x, loc.y, loc.z);
		if(method == 0)
			cc.disengage = true;
		if(method == 1)
			cc.engage = true;
		cc.markDirty();
		loc.worldObj.markBlockForUpdate(loc.x, loc.y, loc.z);
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void detach(IComputerAccess computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equals(IPeripheral other) {
		// TODO Auto-generated method stub
		return false;
	}

}
