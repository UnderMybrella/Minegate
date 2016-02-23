package org.abimon.mods.minecraft.minegate.compat.waila;

import java.util.LinkedList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WAILAStargateController implements IWailaDataProvider {
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP arg0, TileEntity te, NBTTagCompound nbt, World arg3, int arg4,
			int arg5, int arg6) {
		te.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public List<String> getWailaBody(ItemStack arg0, List<String> arg1, IWailaDataAccessor data,
			IWailaConfigHandler arg3) {
		List<String> list = new LinkedList<String>();
		list.add("Stored Naquity: " + data.getNBTData().getFloat("Naquity"));
		list.add("Naquity Cost: " + data.getNBTData().getInteger("Cost"));
		return list;
	}

	@Override
	public List<String> getWailaHead(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2,
			IWailaConfigHandler arg3) {
		return arg1;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor arg0, IWailaConfigHandler arg1) {
		return new ItemStack(arg0.getBlock());
	}

	@Override
	public List<String> getWailaTail(ItemStack arg0, List<String> arg1, IWailaDataAccessor arg2,
			IWailaConfigHandler arg3) {
		return arg1;
	}

}
