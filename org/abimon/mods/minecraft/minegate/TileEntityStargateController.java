package org.abimon.mods.minecraft.minegate;

import java.util.LinkedList;
import java.util.Random;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityStargateController extends TileEntity
{
	LinkedList<Location> frameBlocks = new LinkedList<Location>();

	Glyph[] address;
	
	EnumDirection direction = EnumDirection.UNKNOWN;

	public void checkFrame() {
		iterateFrame(xCoord, yCoord, zCoord);
		System.out.println(frameBlocks);
	}

	public void iterateFrame(int x, int y, int z){

		if(frameBlocks.contains(new Location(x, y, z, worldObj)))
			return;

		frameBlocks.add(new Location(x, y, z, worldObj));

		Block up = worldObj.getBlock(x, y+1, z);
		Block down = worldObj.getBlock(x, y-1, z);
		Block xUp = worldObj.getBlock(x+1, y, z);
		Block xDown = worldObj.getBlock(x-1, y, z);
		Block zUp = worldObj.getBlock(x, y, z+1);
		Block zDown = worldObj.getBlock(x, y, z-1);

		if(up == MineGate.stargateFrame || up == MineGate.glyphBlock || up == MineGate.chevron)
			iterateFrame(x, y+1, z);
		if(down == MineGate.stargateFrame || down == MineGate.glyphBlock || down == MineGate.chevron)
			iterateFrame(x, y-1, z);
		if(xUp == MineGate.stargateFrame || xUp == MineGate.glyphBlock || xUp == MineGate.chevron)
			iterateFrame(x+1, y, z);
		if(xDown == MineGate.stargateFrame || xDown == MineGate.glyphBlock || xDown == MineGate.chevron)
			iterateFrame(x-1, y, z);
		if(zUp == MineGate.stargateFrame || zUp == MineGate.glyphBlock || zUp == MineGate.chevron)
			iterateFrame(x, y, z+1);
		if(zDown == MineGate.stargateFrame || zDown == MineGate.glyphBlock || zDown == MineGate.chevron)
			iterateFrame(x, y, z-1);
	}

	public void dialOut(){

		Location otherController = null;

		LinkedList<Glyph> glyphs = new LinkedList<Glyph>();
		for(Location loc : frameBlocks)
			if(worldObj.getBlock(loc.x, loc.y, loc.z) == MineGate.chevron)
			{
				try{
					glyphs.add(((TileEntityChevron) worldObj.getTileEntity(loc.x, loc.y, loc.z)).glyph); 
				}
				catch(Throwable th){}
			}
		
		otherController = MineGate.addressToController.get(glyphs.toArray(new Glyph[0]));
		
		if(otherController == null){
			System.err.println("No controller...");
			System.out.println(glyphs);
			for(Glyph[] glyphList : MineGate.addressToController.keySet())
			{
				boolean matches = true;
				if(glyphList == null)
					continue;
				if(glyphList.length != glyphs.size())
					continue;
				for(int i = 0; i < glyphList.length; i++)
					if(!glyphList[i].equals(glyphs.get(i)))
					{
						matches = false;
						break;
					}
				if(matches)
				{
					otherController = MineGate.addressToController.get(glyphList);
					System.out.println("Matched");
					break;
				}
			}
			if(otherController == null)
				return;
			System.out.println("Made it");
		}
		
		TileEntityStargateController sgc = null;
		if(otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z) instanceof TileEntityStargateController)
			sgc = (TileEntityStargateController) otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z);
		else
			return;

		int highestX = Integer.MIN_VALUE;
		int highestY = Integer.MIN_VALUE;
		int highestZ = Integer.MIN_VALUE;

		int lowestX = Integer.MAX_VALUE;
		int lowestY = Integer.MAX_VALUE;
		int lowestZ = Integer.MAX_VALUE;

		for(Location loc : frameBlocks){
			if(loc.x > highestX)
				highestX = loc.x;
			if(loc.y > highestY)
				highestY = loc.y;
			if(loc.z > highestZ)
				highestZ = loc.z;

			if(loc.x < lowestX)
				lowestX = loc.x;
			if(loc.y < lowestY)
				lowestY = loc.y;
			if(loc.z < lowestZ)
				lowestZ = loc.z;
		}

		int avgX = (highestX + lowestX) / 2;
		int avgY = (highestY + lowestY) / 2;
		int avgZ = (highestZ + lowestZ) / 2;

		LinkedList<Location> locations = new LinkedList<Location>();

		locations.addAll(frameBlocks);

		System.out.println("Steady now...");
		
		while(locations.size() > 0)
			for(int i = 0; i < locations.size(); i++)
			{
				Location loc = locations.get(i);

				System.out.println(locations.size() + ":" + loc.x + ":" + avgX + ":" + loc.y + ":" + avgY + ":" + loc.z + ":" + avgZ);
				for(boolean[] key : new boolean[][]{{true, true, false}, {false, true, true}, {true, false, true}}){

					int tmpX = loc.x;
					int tmpY = loc.y;
					int tmpZ = loc.z;

					if(tmpX < avgX && key[0])
						tmpX++;
					if(tmpY < avgY && key[1])
						tmpY++;
					if(tmpZ < avgZ && key[2])
						tmpZ++;

					if(tmpX > avgX && key[0])
						tmpX--;
					if(tmpY > avgY && key[1])
						tmpY--;
					if(tmpZ > avgZ && key[2])
						tmpZ--;

					if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateController && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateFrame && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.glyphBlock && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.chevron)
					{
						int framesHit = 0;

						for(int tmptX = tmpX; tmptX < tmpX + 64; tmptX++)
							if(worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptX = tmpX; tmptX > tmpX - 64; tmptX--)
							if(worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY < tmpY + 64; tmptY++)
							if(worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY > tmpY - 64; tmptY--)
							if(worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ < tmpZ + 64; tmptZ++)
							if(worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ > tmpZ - 64; tmptZ--)
							if(worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						if(framesHit >= 4){
							if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargatePortal){
								worldObj.setBlock(tmpX, tmpY, tmpZ, MineGate.stargatePortal);
								TileEntityStargatePortal portal = new TileEntityStargatePortal();
								portal.diallingTo = otherController;
								portal.diallingFrom = new Location(this);
								worldObj.setTileEntity(tmpX, tmpY, tmpZ, portal);
								locations.add(new Location(tmpX, tmpY, tmpZ, worldObj));
							}
							else
							{
								TileEntityStargatePortal portal = new TileEntityStargatePortal();
								World obj = null;
								for(WorldServer server : MinecraftServer.getServer().worldServers)
									if(server.provider.dimensionId == 1)
										obj = server;
								portal.diallingTo = otherController;
								portal.diallingFrom = new Location(this);
								worldObj.setTileEntity(tmpX, tmpY, tmpZ, portal);
							}
						}

						if(tmpX == avgX)
							if(tmpY == avgY)
								if(tmpZ == avgZ)
									break;
					}
				}
				locations.remove(loc);
				if(loc.x == avgX)
					if(loc.y == avgY)
						if(loc.z == avgZ)
							continue;
			}
		
		System.out.println("Getting there...");
		
		sgc.checkFrame();
		sgc.dialIn();
	}
	
	public void dialIn(){
		
		System.out.println(this + ": Dialling in");
		int highestX = Integer.MIN_VALUE;
		int highestY = Integer.MIN_VALUE;
		int highestZ = Integer.MIN_VALUE;

		int lowestX = Integer.MAX_VALUE;
		int lowestY = Integer.MAX_VALUE;
		int lowestZ = Integer.MAX_VALUE;

		for(Location loc : frameBlocks){
			if(loc.x > highestX)
				highestX = loc.x;
			if(loc.y > highestY)
				highestY = loc.y;
			if(loc.z > highestZ)
				highestZ = loc.z;

			if(loc.x < lowestX)
				lowestX = loc.x;
			if(loc.y < lowestY)
				lowestY = loc.y;
			if(loc.z < lowestZ)
				lowestZ = loc.z;
		}

		int avgX = (highestX + lowestX) / 2;
		int avgY = (highestY + lowestY) / 2;
		int avgZ = (highestZ + lowestZ) / 2;

		LinkedList<Location> locations = new LinkedList<Location>();

		locations.addAll(frameBlocks);

		while(locations.size() > 0)
			for(int i = 0; i < locations.size(); i++)
			{
				Location loc = locations.get(i);

				for(boolean[] key : new boolean[][]{{true, true, false}, {false, true, true}, {true, false, true}}){

					int tmpX = loc.x;
					int tmpY = loc.y;
					int tmpZ = loc.z;

					if(tmpX < avgX && key[0])
						tmpX++;
					if(tmpY < avgY && key[1])
						tmpY++;
					if(tmpZ < avgZ && key[2])
						tmpZ++;

					if(tmpX > avgX && key[0])
						tmpX--;
					if(tmpY > avgY && key[1])
						tmpY--;
					if(tmpZ > avgZ && key[2])
						tmpZ--;

					if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateController && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateFrame && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.glyphBlock && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.chevron)
					{
						int framesHit = 0;

						for(int tmptX = tmpX; tmptX < tmpX + 64; tmptX++)
							if(worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptX = tmpX; tmptX > tmpX - 64; tmptX--)
							if(worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmptX, tmpY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY < tmpY + 64; tmptY++)
							if(worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY > tmpY - 64; tmptY--)
							if(worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmptY, tmpZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ < tmpZ + 64; tmptZ++)
							if(worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ > tmpZ - 64; tmptZ--)
							if(worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateController || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.stargateFrame || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.glyphBlock || worldObj.getBlock(tmpX, tmpY, tmptZ) == MineGate.chevron)
							{
								framesHit++;
								break;
							}

						if(framesHit >= 4){
							if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargatePortal){
								worldObj.setBlock(tmpX, tmpY, tmpZ, MineGate.stargatePortal, 1, 3);
								worldObj.removeTileEntity(tmpX, tmpY, tmpZ);
								worldObj.setBlockMetadataWithNotify(tmpX, tmpY, tmpZ, 1, 3);
								locations.add(new Location(tmpX, tmpY, tmpZ, worldObj));
							}
							else
							{
								worldObj.removeTileEntity(tmpX, tmpY, tmpZ);
								worldObj.setBlockMetadataWithNotify(tmpX, tmpY, tmpZ, 1, 3);
							}
						}

						if(tmpX == avgX)
							if(tmpY == avgY)
								if(tmpZ == avgZ)
									break;
					}
				}
				locations.remove(loc);
				if(loc.x == avgX)
					if(loc.y == avgY)
						if(loc.z == avgZ)
							continue;
			}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		MineGate.addToRegister(this);
		
		direction = EnumDirection.valueOf(nbt.getString("Direction"));

		try{
			int[] intAddress = nbt.hasKey("Address") ? nbt.getIntArray("Address") : new int[0];

			address = new Glyph[intAddress.length];

			for(int i = 0; i < address.length; i++)
				address[i] = MineGate.glyphList.get(intAddress[i]);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		
		nbt.setString("Direction", direction.name());

		try{
			int[] intAddress = new int[address.length];

			for(int i = 0; i < intAddress.length; i++)
				for(int j = 0; j < MineGate.glyphList.size(); j++)
					if(MineGate.glyphList.get(j).equals(address[i]))
					{
						intAddress[i] = j;
						break;
					}

			nbt.setIntArray("Address", intAddress);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		
		System.out.println(nbt);
	}

}
