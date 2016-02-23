package org.abimon.mods.minecraft.minegate.compat;

import java.util.LinkedList;
import java.util.List;

import org.abimon.mods.minecraft.minegate.blocks.BlockChevron;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquadahReactor;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquityProvider;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargateController;
import org.abimon.mods.minecraft.minegate.compat.waila.WAILAChevron;
import org.abimon.mods.minecraft.minegate.compat.waila.WAILANaquadahReactorFuel;
import org.abimon.mods.minecraft.minegate.compat.waila.WAILANaquityProvider;
import org.abimon.mods.minecraft.minegate.compat.waila.WAILAStargateController;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WAILACompatibility{

	public static boolean loaded = false;

	public WAILACompatibility(){
		
		loaded = true;
		
		registerAll(BlockStargateController.class, new WAILAStargateController());
		registerAll(BlockChevron.class, new WAILAChevron());
		registerAll(BlockNaquityProvider.class, new WAILANaquityProvider());
		registerAll(BlockNaquadahReactor.class, new WAILANaquityProvider());
		registerAll(BlockNaquadahReactor.class, new WAILANaquadahReactorFuel());
	}

	public void registerAll(Class clazz, IWailaDataProvider prov){
		ModuleRegistrar.instance().registerHeadProvider(prov, clazz);
		ModuleRegistrar.instance().registerBodyProvider(prov, clazz);
		ModuleRegistrar.instance().registerTailProvider(prov, clazz);
		ModuleRegistrar.instance().registerNBTProvider(prov, clazz);
	}
}
