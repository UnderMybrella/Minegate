package org.abimon.mods.minecraft.minegate.tileentity;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.EnumDirection;
import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.IStargateLocationOverride;
import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityStargateController extends TileEntity
{
	public LinkedList<Location> frameBlocks = new LinkedList<Location>();

	public Glyph[] address;
	public Glyph[] engagedWith;

	public EnumDirection direction = EnumDirection.UNKNOWN;

	public long openFor = -1L;

	public int matterTicks = -1;

	public int avgX;
	public int avgY;
	public int avgZ;

	public float naquityStored = 0;

	public float naquityCost = 2;
	public float prevCost = 2;

	boolean loaded = false;

	public void updateEntity() {

//		if(!loaded)
//		{
//			loaded = true;
//		}
		checkFrame();

		for(Location loc : frameBlocks)
			if(loc.worldObj.getTileEntity(loc.x, loc.y, loc.z) instanceof INaquityProvider)
			{
				INaquityProvider prov = (INaquityProvider) loc.worldObj.getTileEntity(loc.x, loc.y, loc.z);
				float taken = prov.retrieveNaquity(prov.naquityContent());
				naquityStored += taken;
				//System.out.println(naquityStored + ":" + loc);
			}
			else if(loc.worldObj.getTileEntity(loc.x, loc.y, loc.z) instanceof TileEntityControl)
			{
				TileEntityControl cc = (TileEntityControl) loc.worldObj.getTileEntity(loc.x, loc.y, loc.z);
				if(cc.disengage)
					if(openFor > 0)
					{
						Location otherController = getOtherController();
						if(otherController != null && otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z) instanceof TileEntityStargateController){
							TileEntityStargateController sgOne = (TileEntityStargateController) otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z);
							sgOne.disengage();
						}
						disengage();
						engagedWith = null;
						cc.disengage = false;
					}

				if(cc.engage)
				{
					//System.out.println(openFor + ", " + engagedWith);
					if(openFor < 0 && naquityStored >= 10){
						if(engagedWith == null)
							dialOut();
						cc.engage = false;
					}
				}
			}
		if(openFor == -1)
			return;
		else
			openFor++;
		if(matterTicks >= 2400)
		{
			try{
				Location otherController = getOtherController();
				if(otherController != null && otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z) instanceof TileEntityStargateController){
					TileEntityStargateController sgOne = (TileEntityStargateController) otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z);
					sgOne.disengage();
				}
				disengage();
				engagedWith = null;
			}
			catch(Throwable th){}
			matterTicks = -1;
		}
		else if(matterTicks >= 0)
			matterTicks++;

		naquityStored -= ((float) naquityCost / 1200.0f);

		if(naquityStored <= 0)
		{
			try{
				Location otherController = getOtherController();
				if(otherController != null && otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z) instanceof TileEntityStargateController){
					TileEntityStargateController sgOne = (TileEntityStargateController) otherController.worldObj.getTileEntity(otherController.x, otherController.y, otherController.z);
					float difference = naquityStored * -1.0f;
					sgOne.naquityStored -= difference;
					naquityStored += difference;
					if(sgOne.naquityStored <= 0)
					{
						disengage();
						engagedWith = null;
						sgOne.disengage();
						openFor = -1;
					}
				}
				else
				{
					disengage();
					engagedWith = null;
					openFor = -1;
				}
			}
			catch(Throwable th){
				openFor = -1;
			}
		}

		if(openFor % 1200 == 0){ //Increase Cost
			float tmp = naquityCost + ((int) prevCost / 2);
			prevCost = naquityCost;
			naquityCost = tmp;
		}

	}

	public void checkFrame() {
		frameBlocks.clear();
		iterateFrame(xCoord, yCoord, zCoord);

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

		avgX = (highestX + lowestX) / 2;
		avgY = (highestY + lowestY) / 2;
		avgZ = (highestZ + lowestZ) / 2;
	}

	public void iterateFrame(int x, int y, int z){

		if(frameBlocks.contains(new Location(x, y, z, worldObj)))
			return;

		if(isValidBlock(worldObj, worldObj.getBlock(x, y, z))) //Double check everything!
			frameBlocks.add(new Location(x, y, z, worldObj));

		Block up = worldObj.getBlock(x, y+1, z);
		Block down = worldObj.getBlock(x, y-1, z);
		Block xUp = worldObj.getBlock(x+1, y, z);
		Block xDown = worldObj.getBlock(x-1, y, z);
		Block zUp = worldObj.getBlock(x, y, z+1);
		Block zDown = worldObj.getBlock(x, y, z-1);

		if(isValidBlock(worldObj, up))
			iterateFrame(x, y+1, z);
		if(isValidBlock(worldObj, down))
			iterateFrame(x, y-1, z);
		if(isValidBlock(worldObj, xUp))
			iterateFrame(x+1, y, z);
		if(isValidBlock(worldObj, xDown))
			iterateFrame(x-1, y, z);
		if(isValidBlock(worldObj, zUp))
			iterateFrame(x, y, z+1);
		if(isValidBlock(worldObj, zDown))
			iterateFrame(x, y, z-1);
	}

	public static boolean isValidBlock(World worldObj, Block block){
		return block == MineGate.glyphBlock || block instanceof INaquadahBlock || (block instanceof ITileEntityProvider && ((ITileEntityProvider) block).createNewTileEntity(worldObj, 0) instanceof INaquityProvider);
	}

	public Location getOtherController(){
		Location otherController = null;

		LinkedList<Glyph> glyphs = new LinkedList<Glyph>();
		if(engagedWith == null){
			for(Location loc : frameBlocks)
				if(worldObj.getBlock(loc.x, loc.y, loc.z) == MineGate.chevron)
				{
					try{
						TileEntityChevron chev = ((TileEntityChevron) worldObj.getTileEntity(loc.x, loc.y, loc.z));
						while(glyphs.size() < chev.glyphLocation + 1)
							glyphs.add(null);
						glyphs.set(chev.glyphLocation, chev.glyph);
					}
					catch(Throwable th){}
				}
		}
		else
			for(Glyph glyph : engagedWith)
				glyphs.add(glyph);

		for(int i = 0; i < glyphs.size(); i++)
		{	
			if(glyphs.get(i) == null)
			{
				glyphs.remove(i);
				i = 0;
			}
		}

		otherController = MineGate.addressToController.get(glyphs.toArray(new Glyph[0]));

		if(otherController == null){
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
					break;
				}
			}
			if(otherController == null)
				return null;
		}
		return otherController;
	}

	public boolean dialOut(){

		checkFrame();

		engagedWith = null;
		Location diallingLocation = getOtherController();

		boolean usingCustomLocation = false;
		int highestPriority = 10;

		if(diallingLocation == null)
			highestPriority = 0;

		for(Location loc : frameBlocks){
			if(loc.worldObj == null)
				continue;
			TileEntity te = loc.worldObj.getTileEntity(loc.x, loc.y, loc.z);
			if(te != null && te instanceof IStargateLocationOverride){
				IStargateLocationOverride slo = (IStargateLocationOverride) te;
				if(slo.getPriority() > highestPriority){
					Location newLoc = slo.getLocationOverride(worldObj, xCoord, yCoord, zCoord);
					if(newLoc != null){
						highestPriority = slo.getPriority();
						diallingLocation = newLoc;
						usingCustomLocation = true;
					}
				}
			}
		}

		if(diallingLocation == null && !usingCustomLocation)
			return false;

		TileEntityStargateController sgc = null;
		if(diallingLocation.worldObj.getTileEntity(diallingLocation.x, diallingLocation.y, diallingLocation.z) instanceof TileEntityStargateController)
			sgc = (TileEntityStargateController) diallingLocation.worldObj.getTileEntity(diallingLocation.x, diallingLocation.y, diallingLocation.z);

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

					if(!isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmpZ)))
					{
						int framesHit = 0;

						for(int tmptX = tmpX; tmptX < tmpX + 64; tmptX++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptX = tmpX; tmptX > tmpX - 64; tmptX--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY < tmpY + 64; tmptY++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY > tmpY - 64; tmptY--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ < tmpZ + 64; tmptZ++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ > tmpZ - 64; tmptZ--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
								framesHit++;
								break;
							}

						if(framesHit >= 4){
							if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargatePortal){
								worldObj.setBlock(tmpX, tmpY, tmpZ, MineGate.stargatePortal);
								TileEntityStargatePortal portal = new TileEntityStargatePortal();
								portal.diallingTo = diallingLocation;
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
								portal.diallingTo = diallingLocation;
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

		this.openFor = 0L;
		this.matterTicks = 0;

		this.naquityCost = 2;
		this.prevCost = 2;

		if(sgc != null){
			sgc.checkFrame();
			sgc.dialIn();
			sgc.engagedWith = this.address;
			this.engagedWith = sgc.address;
		}
		naquityStored -= 10;

		return true;
	}

	public void dialIn(){
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

					if(!isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmpZ)))
					{
						int framesHit = 0;

						for(int tmptX = tmpX; tmptX < tmpX + 64; tmptX++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptX = tmpX; tmptX > tmpX - 64; tmptX--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY < tmpY + 64; tmptY++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY > tmpY - 64; tmptY--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ < tmpZ + 64; tmptZ++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ > tmpZ - 64; tmptZ--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
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

	public void disengage(){
		openFor = -1;
		matterTicks = -1;
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

					if(worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateController && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.stargateFrame && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.glyphBlock && worldObj.getBlock(tmpX, tmpY, tmpZ) != MineGate.chevron && !(worldObj.getBlock(tmpX, tmpY, tmpZ) instanceof INaquadahBlock))
					{
						int framesHit = 0;

						for(int tmptX = tmpX; tmptX < tmpX + 64; tmptX++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptX = tmpX; tmptX > tmpX - 64; tmptX--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmptX, tmpY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY < tmpY + 64; tmptY++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptY = tmpY; tmptY > tmpY - 64; tmptY--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmptY, tmpZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ < tmpZ + 64; tmptZ++)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
								framesHit++;
								break;
							}

						for(int tmptZ = tmpZ; tmptZ > tmpZ - 64; tmptZ--)
							if(isValidBlock(worldObj, worldObj.getBlock(tmpX, tmpY, tmptZ))){
								framesHit++;
								break;
							}

						if(framesHit >= 4){
							if(worldObj.getBlock(tmpX, tmpY, tmpZ) != Blocks.air){
								worldObj.removeTileEntity(tmpX, tmpY, tmpZ);
								worldObj.setBlock(tmpX, tmpY, tmpZ, Blocks.air);
								locations.add(new Location(tmpX, tmpY, tmpZ, worldObj));
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

		this.openFor = nbt.getLong("OpenTicks");
		this.matterTicks = nbt.getInteger("MatterTicks");

		naquityStored = Math.max(0, nbt.getFloat("Naquity"));

		naquityCost = nbt.getFloat("Cost");
		prevCost = nbt.getFloat("PrevCost");
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
		}

		nbt.setLong("OpenTicks", this.openFor);
		nbt.setInteger("MatterTicks", this.matterTicks);

		nbt.setFloat("Naquity", this.naquityStored);

		nbt.setFloat("Cost", naquityCost);
		nbt.setFloat("PrevCost", prevCost);
	}

	public void onChunkUnload()
	{
		loaded = false;
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.func_148857_g());
	}

}
