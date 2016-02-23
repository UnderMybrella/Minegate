package org.abimon.mods.minecraft.minegate.compat.waila;

import java.util.List;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WAILANaquadahReactorFuel extends WAILABasic{

	@Override
	public List<String> getWailaBody(ItemStack item, List<String> list, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		list.add("Fuel: " + accessor.getNBTData().getFloat("Fuel"));
		list.add("Consumption: " + accessor.getNBTData().getFloat("Consumption"));
		list.add("Production: " + accessor.getNBTData().getFloat("Production"));
		return super.getWailaBody(item, list, accessor, config);
	}
	
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
			int y, int z) {
		if(te instanceof TileEntityNaquadahReactor){
			TileEntityNaquadahReactor tenr = (TileEntityNaquadahReactor) te;
			tag.setFloat("Fuel", tenr.getFuel());
			tag.setFloat("Consumption", tenr.getConsumption());
			tag.setFloat("Production", tenr.getProduction());
		}
		return super.getNBTData(player, te, tag, world, x, y, z);
	}
}
