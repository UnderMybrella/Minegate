package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;

import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class ComputerCraftCompatibility implements IPeripheralProvider 
{
	public static final Block ccPeripheral = new BlockCC().setCreativeTab(MineGate.creative).setBlockName(MineGate.MODID + ":ccPeripheral").setBlockTextureName(MineGate.MODID + ":ccPeripheral");
	
	public ComputerCraftCompatibility(){
		GameRegistry.registerBlock(ccPeripheral, "ccPeripheral");
		
		ComputerCraftAPI.registerPeripheralProvider(this);
		
	}

	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		IPeripheral peripheral = null;
		try{
			if(world.getBlock(x, y, z) == ccPeripheral)
				peripheral = new FramePeripheral(new Location(x, y, z, world));
			if(world.getBlock(x, y, z) == MineGate.dialHomeDevice)
				peripheral = new DHDPeripheral(new Location(x, y, z, world));
			if(world.getBlock(x, y, z) == MineGate.dhdGhost){
				int meta = world.getBlockMetadata(x, y, z);
				if(meta == 0)
					x--;
				if(meta == 1)
				{
					x--;
					z--;
				}
				if(meta == 2)
					z--;
				if(meta == 3)
				{
					x++;
					z--;
				}
				if(meta == 4)
					x++;
				if(meta == 5)
				{
					x++;
					z++;
				}
				if(meta == 6)
					z++;
				if(meta == 7)
				{
					x--;
					z++;
				}
				peripheral = new DHDPeripheral(new Location(x, y, z, world));
			}
		}
		catch(Throwable th){}
		return peripheral;
	}
}
