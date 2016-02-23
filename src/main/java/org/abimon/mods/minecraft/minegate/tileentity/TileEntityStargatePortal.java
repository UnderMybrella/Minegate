package org.abimon.mods.minecraft.minegate.tileentity;

import java.awt.Color;
import java.util.Random;

import org.abimon.mods.minecraft.minegate.EnumDirection;
import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.MineGate;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.entity.EntityManaBurst;

@InterfaceList(value = {@Interface(iface = "vazkii.botania.api.mana.IManaReceiver", modid = "Botania"), @Interface(iface = "vazkii.botania.api.mana.IManaCollisionGhost", modid = "Botania")})
public class TileEntityStargatePortal extends TileEntity implements IManaReceiver//, IManaCollisionGhost
{
	public Location diallingTo;
	public Location diallingFrom;
	
	int dim;
	int dimED;

	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		int dim = nbt.hasKey("WorldObj-Dialling") ? nbt.getInteger("WorldObj-Dialling") : 0;

		World worldObj = null;

		for(WorldServer server : MinecraftServer.getServer().worldServers)
			if(dim == server.provider.dimensionId)
			{
				worldObj = server;
				break;
			}

		diallingTo = new Location(nbt.getInteger("X-Dialling"), nbt.getInteger("Y-Dialling"), nbt.getInteger("Z-Dialling"), worldObj);

		int dimED = nbt.hasKey("WorldObj-Dialled") ? nbt.getInteger("WorldObj-Dialled") : 0;

		World worldObjED = null;

		for(WorldServer server : MinecraftServer.getServer().worldServers)
			if(dimED == server.provider.dimensionId)
			{
				worldObjED = server;
				break;
			}

		diallingFrom = new Location(nbt.getInteger("X-Dialled"), nbt.getInteger("Y-Dialled"), nbt.getInteger("Z-Dialled"), worldObjED);
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		if(diallingTo != null){
			nbt.setInteger("X-Dialling", diallingTo.x);
			nbt.setInteger("Y-Dialling", diallingTo.y);
			nbt.setInteger("Z-Dialling", diallingTo.z);
			nbt.setInteger("WorldObj-Dialling", diallingFrom.worldObj != null ? diallingTo.worldObj.provider.dimensionId : dim);
		}

		if(diallingFrom != null){
			nbt.setInteger("X-Dialled", diallingFrom.x);
			nbt.setInteger("Y-Dialled", diallingFrom.y);
			nbt.setInteger("Z-Dialled", diallingFrom.z);
			nbt.setInteger("WorldObj-Dialled", diallingFrom.worldObj != null ? diallingFrom.worldObj.provider.dimensionId : dimED);
		}
	}

	public void teleportEntity(Entity entity){

		if(diallingTo == null || diallingFrom == null)
			return;

		TileEntityStargateController sgc = null;
		if(this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z) instanceof TileEntityStargateController)
			sgc = (TileEntityStargateController) this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z);
		if(sgc == null)
		{
			//Temporarily disabled die to Mystcraft books
//			if(entity instanceof EntityLivingBase)
//				if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
//					MineGate.vaporiseEntity(entity);
//				else;
//			else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
//				MineGate.vaporiseEntity(entity);
			
			if(entity.dimension != this.diallingTo.worldObj.provider.dimensionId)
				MineGate.teleportEntity(entity, this.diallingTo.worldObj.provider.dimensionId);

			if(entity instanceof EntityLivingBase)
			{
				EntityLivingBase base = (EntityLivingBase) entity;
				if(base.getActivePotionEffect(MineGate.wormholeEffect) == null){
					base.setPositionAndUpdate(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z);
					base.addPotionEffect(new PotionEffect(MineGate.wormholeEffect.id, 80, 0));
				}
			}
			else
				entity.setPosition(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z);
			entity.hurtResistantTime = 80;
			
			return;
		}

		TileEntityStargateController sgOne = null;
		if(this.diallingFrom.worldObj.getTileEntity(this.diallingFrom.x, this.diallingFrom.y, this.diallingFrom.z) instanceof TileEntityStargateController)
			sgOne = (TileEntityStargateController) this.diallingFrom.worldObj.getTileEntity(this.diallingFrom.x, this.diallingFrom.y, this.diallingFrom.z);
		if(sgOne == null)
		{
			if(entity instanceof EntityLivingBase)
				if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
					MineGate.vaporiseEntity(entity);
				else;
			else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
				MineGate.vaporiseEntity(entity);
			return;
		}

		sgc.checkFrame();
		sgOne.checkFrame();

		sgOne.matterTicks = 0;

		int highestXTo = Integer.MIN_VALUE;
		int highestYTo = Integer.MIN_VALUE;
		int highestZTo = Integer.MIN_VALUE;

		int lowestXTo = Integer.MAX_VALUE;
		int lowestYTo = Integer.MAX_VALUE;
		int lowestZTo = Integer.MAX_VALUE;

		for(Location loc : sgc.frameBlocks){
			if(loc.x > highestXTo)
				highestXTo = loc.x;
			if(loc.y > highestYTo)
				highestYTo = loc.y;
			if(loc.z > highestZTo)
				highestZTo = loc.z;

			if(loc.x < lowestXTo)
				lowestXTo = loc.x;
			if(loc.y < lowestYTo)
				lowestYTo = loc.y;
			if(loc.z < lowestZTo)
				lowestZTo = loc.z;
		}

		int highestX = Integer.MIN_VALUE;
		int highestY = Integer.MIN_VALUE;
		int highestZ = Integer.MIN_VALUE;

		int lowestX = Integer.MAX_VALUE;
		int lowestY = Integer.MAX_VALUE;
		int lowestZ = Integer.MAX_VALUE;

		for(Location loc : sgOne.frameBlocks){
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
			{
				//System.out.println(loc.z);
				//System.out.println(loc.worldObj.getBlock(loc.x, loc.y, loc.z));
				lowestZ = loc.z;
			}
		}

		float relX = (float) (entity.posX - lowestX);
		float relY = (float) (entity.posY - lowestY);
		float relZ = (float) (entity.posZ - lowestZ);
		
		//System.out.println(entity.posZ + ", " + lowestZ);

		int lengthOne = highestX - lowestX;
		int lengthTwo = highestXTo - lowestXTo;

		int heightOne = highestY - lowestY;
		int heightTwo = highestYTo - lowestYTo;

		int depthOne = highestZ - lowestZ;
		int depthTwo = highestZTo - lowestZTo;

		float modLength = ((float) lengthTwo) / ((float) lengthOne);
		float modHeight = ((float) heightTwo) / ((float) heightOne);
		float modDepth = ((float) depthTwo) / ((float) depthOne);

		if(!Float.isFinite(modLength) || Float.isInfinite(modLength) || modLength == 0.0f)
			modLength = 1.0f;
		if(!Float.isFinite(modHeight) || Float.isInfinite(modLength) || modHeight == 0.0f)
			modHeight = 1.0f;
		if(!Float.isFinite(modDepth) || Float.isInfinite(modLength) || modDepth == 0.0f)
			modDepth = 1.0f;

//		//System.out.println(relX + ":" + relY + ":" + relZ);
//		//System.out.println(modLength + ":" + modHeight + ":" + modDepth);
//		//System.out.println(lowestXTo + ":" + lowestYTo + ":" + lowestZTo);
//
//		//System.out.println(new Location(relX, relY, relZ));

		EnumDirection fromShape = sgOne.direction; //Dialling From
		EnumDirection toShape = sgc.direction; //Dialling To
		
		if(fromShape == EnumDirection.UNKNOWN || toShape == EnumDirection.UNKNOWN || fromShape == null || toShape == null)
			if(entity instanceof EntityPlayer)
			{
				((EntityPlayer) entity).addChatMessage(new ChatComponentText("One of the Stargates in this connection is configured incorrectly."));
				((EntityPlayer) entity).addChatMessage(new ChatComponentText("Right-Click the controller to configure the direction of the Stargate."));
				((EntityPlayer) entity).addChatMessage(new ChatComponentText("Be aware this can result in teleportation issues between Stargates."));
			}
			else;

		if(fromShape == null || fromShape == EnumDirection.UNKNOWN)
			fromShape = EnumDirection.getShapeOfStargate(sgOne);
		if(toShape == null || toShape == EnumDirection.UNKNOWN)
			toShape = EnumDirection.getShapeOfStargate(sgc);

		if(fromShape != EnumDirection.UNKNOWN && toShape != EnumDirection.UNKNOWN)
		{
			double tmpMotion = toShape.isXAxis() ? entity.motionX : toShape.isYAxis() ? entity.motionY : entity.motionZ;
			toShape.setMotion(entity, fromShape.isXAxis() ? entity.motionX : fromShape.isYAxis() ? entity.motionY : entity.motionZ);
			fromShape.setMotion(entity, tmpMotion);

			////System.out.println(modLength + ":" + modHeight + ":" + modDepth);

			float tmpMod = toShape.isXAxis() ? modLength : toShape.isYAxis() ? modHeight : modDepth;
			if(toShape.isXAxis())
				modLength = fromShape.isXAxis() ? modLength : fromShape.isYAxis() ? modHeight : modDepth;
			if(toShape.isYAxis())
				modHeight = fromShape.isXAxis() ? modLength : fromShape.isYAxis() ? modHeight : modDepth;
			if(toShape.isZAxis())
				modDepth = fromShape.isXAxis() ? modLength : fromShape.isYAxis() ? modHeight : modDepth;

			if(fromShape.isXAxis())
				modLength = tmpMod;
			if(fromShape.isYAxis())
				modHeight = tmpMod;
			if(fromShape.isZAxis())
				modDepth = tmpMod;

			float tmpLowest = toShape.isXAxis() ? relX : toShape.isYAxis() ? relY : relZ;
			if(toShape.isXAxis())
				relX = fromShape.isXAxis() ? relX : fromShape.isYAxis() ? relY : relZ;
			if(toShape.isYAxis())
				relY = fromShape.isXAxis() ? relX : fromShape.isYAxis() ? relY : relZ;
			if(toShape.isZAxis())
				relZ = fromShape.isXAxis() ? relX : fromShape.isYAxis() ? relY : relZ;

			if(fromShape.isXAxis())
				relX = (int) tmpLowest;
			if(fromShape.isYAxis())
				relY = (int) tmpLowest;
			if(fromShape.isZAxis())
				relZ = (int) tmpLowest;

			//float tmpSide = 

			toShape.setRotation(entity);
		}

		relX *= modLength;
		relY *= modHeight;
		relZ *= modDepth;

		relX += lowestXTo;
		relY += lowestYTo;
		relZ += lowestZTo;

//		for(Location location : sgc.frameBlocks){
//			if(location.x == (int) relX)
//				if(location.x < sgc.avgX)
//					relX++;
//				else
//					relX--;			
//			if(location.y == (int) relY)
//				if(location.y < sgc.avgY)
//					relY++;
//				else
//					relY--;
//			if(location.z == (int) relZ)
//				if(location.z < sgc.avgZ)
//					relZ++;
//				else
//					relZ--;
//		}

		if(entity.dimension != this.diallingTo.worldObj.provider.dimensionId)
			MineGate.teleportEntity(entity, this.diallingTo.worldObj.provider.dimensionId);

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase base = (EntityLivingBase) entity;
			if(base.getActivePotionEffect(MineGate.wormholeEffect) == null){
				base.setPositionAndUpdate(relX, relY, relZ);
				base.addPotionEffect(new PotionEffect(MineGate.wormholeEffect.id, 80, 0));
			}
		}
		else
			entity.setPosition(relX, relY, relZ);
		entity.hurtResistantTime = 80;
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	/** Nothing to see here Vazkii ◕_◕ */
	public void recieveMana(int mana) {
		EntityChicken chicken = new EntityChicken(worldObj);
		chicken.setLocationAndAngles(xCoord, yCoord, zCoord, 0, 0);
		teleportEntity(chicken);

		if(diallingTo == null && diallingFrom == null)
			return;

		TileEntityStargateController sgc = null;
		if(this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z) instanceof TileEntityStargateController)
			sgc = (TileEntityStargateController) this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z);

		if(sgc == null)
			return;

		EnumDirection toShape = sgc.direction; //Dialling To
		if(toShape == null)
			toShape = EnumDirection.getShapeOfStargate(sgc);

		int xDifference = (int) (chicken.posX - xCoord);
		int zDifference = (int) (chicken.posZ - zCoord);
		
		if(xDifference < 0)
			xDifference *= -1;

		if(zDifference < 0)
			zDifference *= -1;
		
		EntityManaBurst burst = new EntityManaBurst(worldObj);
		burst.setLocationAndAngles(((int) chicken.posX) + 0.5, ((int) chicken.posY) + 0.5, ((int) chicken.posZ) + 0.5, 0, 0);
		burst.setColor(new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)).getRGB());
		burst.setMana(mana - (xDifference / 10) - (zDifference / 10));
		burst.setStartingMana(mana);
		burst.setMinManaLoss(0);
		burst.setManaLossPerTick(1);
		burst.setGravity(0F);
		burst.setMotion(toShape.isXAxis() ? toShape.isPositive() ? 0.5 : toShape.isNegative() ? -0.5 : 0 : 0, toShape.isYAxis() ? toShape.isPositive() ? 0.5 : toShape.isNegative() ? -0.5 : 0 : 0, toShape.isZAxis() ? toShape.isPositive() ? 0.5 : toShape.isNegative() ? -0.5 : 0 : 0);
		worldObj.spawnEntityInWorld(burst);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}
}
