package org.abimon.mods.minecraft.minegate.compat.waila;

import java.util.LinkedList;
import java.util.List;

import org.abimon.mods.minecraft.minegate.MineGate;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WAILANaquityProvider extends WAILABasic{

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return new ItemStack(MineGate.naquityProvider);
	}

	@Override
	public List<String> getWailaBody(ItemStack paramItemStack, List<String> paramList,
			IWailaDataAccessor data, IWailaConfigHandler paramIWailaConfigHandler) {
		paramList.add("Stored Naquity: " + data.getNBTData().getFloat("Naquity"));
		return paramList;
	}
}
