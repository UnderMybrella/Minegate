package org.abimon.mods.minecraft.minegate.compat;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.blocks.BlockDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import scala.actors.threadpool.Arrays;

public class DHDPeripheral implements IPeripheral{

	Location loc;
	public DHDPeripheral(Location loc){
		this.loc = loc;
	}

	@Override
	public String getType() {
		return "dhd";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"press", "dial", "disengage", "engage"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {

		final TileEntityDHD dhd = (TileEntityDHD) loc.worldObj.getTileEntity(loc.x, loc.y, loc.z);
		if(method == 0) //Press
		{
			if(arguments.length == 0)
				return new Object[]{"No glyph to press!"};
			else
				for(Object o : arguments)
					if(o == null)
						continue;
					else
						if(o instanceof Integer)
						{
							dhd.pressed[(Integer) o] = true;
							dhd.pressedGlyphs.add(dhd.glyphs[(Integer) o]);
							loc.worldObj.markBlockForUpdate(loc.x, loc.y, loc.z);
						}
						else if(o instanceof Double)
						{
							double d = (Double) o;
							dhd.pressed[(int) d] = true;
							dhd.pressedGlyphs.add(dhd.glyphs[(int) d]);
							loc.worldObj.markBlockForUpdate(loc.x, loc.y, loc.z);
						}
						else if(o instanceof String)
							for(int i = 0; i < dhd.glyphs.length; i++)
								if(dhd.glyphs[i] != null)
									if(dhd.glyphs[i].glyphName.equalsIgnoreCase((String) o))
										if(!dhd.pressed[i])
										{
											dhd.pressed[i] = true;
											dhd.pressedGlyphs.add(dhd.glyphs[i]);
											loc.worldObj.markBlockForUpdate(loc.x, loc.y, loc.z);
											break;
										}
										else;
									else;
								else
									System.out.println(o + ":" + o.getClass());
		}
		if(method == 1){
			if(arguments.length == 0)
			{
				String s = ((BlockDHD) loc.worldObj.getBlock(loc.x, loc.y, loc.z)).handleButtonPress(loc.worldObj, loc.x, loc.y, loc.z);
				if(s == null)
					return new Object[]{"Dial Successful"};
				else
					return new Object[]{"Dialling failed - " + s};
			}
		}
		if(method == 2)
		{
			TileEntity te = loc.worldObj.getTileEntity(dhd.boundX, dhd.boundY, dhd.boundZ);
			if(te == null)
				return new Object[]{"Disengage failed - No bound Stargate"};
			if(te instanceof TileEntityStargateController){
				TileEntityStargateController sgc = (TileEntityStargateController) te;
				if(sgc.openFor <= 0)
					return new Object[]{"Disengage failed - Stargate is either not open or is not dialling someone else"};
				sgc.disengage();
				Location otherController = sgc.getOtherController();
				if(otherController != null && otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z) instanceof TileEntityStargateController){
					TileEntityStargateController sgOne = (TileEntityStargateController) otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z);
					sgOne.disengage();
				}
				return new Object[]{"Disengage successful"};
			}
		}
		if(method == 3)
		{
			TileEntity te = loc.worldObj.getTileEntity(dhd.boundX, dhd.boundY, dhd.boundZ);
			if(te == null)
				return new Object[]{"Engage failed - No bound Stargate"};
			if(te instanceof TileEntityStargateController){
				TileEntityStargateController sgc = (TileEntityStargateController) te;
				if(sgc.openFor <= 0)
					return new Object[]{"Engage failed - Stargate has either already dialled out or has been dialled"};
				sgc.dialOut();
				return new Object[]{"Engaged successfully"};
			}
		}
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {

	}

	@Override
	public void detach(IComputerAccess computer) {

	}

	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}

}
