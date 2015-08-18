package org.abimon.mods.minecraft.minegate;

import java.awt.Color;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.entity.EntityManaBurst;

@InterfaceList(value = {@Interface(iface = "vazkii.botania.api.mana.IManaReceiver", modid = "Botania"), @Interface(iface = "vazkii.botania.api.mana.IManaCollisionGhost", modid = "Botania")})
public class TileEntityStargatePortal extends TileEntity implements IManaReceiver//, IManaCollisionGhost
{
	Location diallingTo;
	Location diallingFrom;

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

		diallingFrom = new Location(nbt.getInteger("X-Dialled"), nbt.getInteger("Y-Dialled"), nbt.getInteger("Z-Dialled"), worldObj);
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		if(diallingTo != null){
			nbt.setInteger("X-Dialling", diallingTo.x);
			nbt.setInteger("Y-Dialling", diallingTo.y);
			nbt.setInteger("Z-Dialling", diallingTo.z);
			nbt.setInteger("WorldObj-Dialling", diallingTo.worldObj.provider.dimensionId);
		}

		if(diallingFrom != null){
			nbt.setInteger("X-Dialled", diallingFrom.x);
			nbt.setInteger("Y-Dialled", diallingFrom.y);
			nbt.setInteger("Z-Dialled", diallingFrom.z);
			nbt.setInteger("WorldObj-Dialled", diallingFrom.worldObj.provider.dimensionId);
		}
	}

	public void teleportEntity(Entity entity){

		if(diallingTo == null || diallingFrom == null)
			return;
		
		TileEntityStargateController sgc = null;
		if(this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z) instanceof TileEntityStargateController)
			sgc = (TileEntityStargateController) this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z);
		System.out.println(this.diallingTo.worldObj.getTileEntity(this.diallingTo.x, this.diallingTo.y, this.diallingTo.z) + ":" + sgc);
		if(sgc == null)
		{
			if(entity instanceof EntityLivingBase)
				if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
					MineGate.vaporiseEntity(entity);
				else;
			else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
				MineGate.vaporiseEntity(entity);
			return;
		}

		TileEntityStargateController sgOne = null;
		if(this.diallingFrom.worldObj.getTileEntity(this.diallingFrom.x, this.diallingFrom.y, this.diallingFrom.z) instanceof TileEntityStargateController)
			sgOne = (TileEntityStargateController) this.diallingFrom.worldObj.getTileEntity(this.diallingFrom.x, this.diallingFrom.y, this.diallingFrom.z);
		System.out.println(sgOne + ":" + this.diallingFrom.worldObj.getTileEntity(this.diallingFrom.x, this.diallingFrom.y, this.diallingFrom.z));
		if(sgOne == null)
		{
			if(entity instanceof EntityLivingBase)
				if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
					MineGate.vaporiseEntity(entity);
				else;
			else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
				MineGate.vaporiseEntity(entity);
			else
				System.out.println("Mana Burst");
			return;
		}

		sgc.checkFrame();
		sgOne.checkFrame();

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
				lowestZ = loc.z;
		}

		float relX = (float) (entity.posX - lowestX);
		float relY = (float) (entity.posY - lowestY);
		float relZ = (float) (entity.posZ - lowestZ);

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

		System.out.println(relX + ":" + relY + ":" + relZ);
		System.out.println(modLength + ":" + modHeight + ":" + modDepth);
		System.out.println(lowestXTo + ":" + lowestYTo + ":" + lowestZTo);

		System.out.println(new Location(relX, relY, relZ));

		EnumDirection fromShape = sgOne.direction; //Dialling From
		if(fromShape == null || fromShape == EnumDirection.UNKNOWN)
			fromShape = EnumDirection.getShapeOfStargate(sgOne);
		EnumDirection toShape = sgc.direction; //Dialling To
		if(toShape == null || toShape == EnumDirection.UNKNOWN)
			toShape = EnumDirection.getShapeOfStargate(sgc);

		if(fromShape != EnumDirection.UNKNOWN && toShape != EnumDirection.UNKNOWN)
		{
			double tmpMotion = toShape.isXAxis() ? entity.motionX : toShape.isYAxis() ? entity.motionY : entity.motionZ;
			toShape.setMotion(entity, fromShape.isXAxis() ? entity.motionX : fromShape.isYAxis() ? entity.motionY : entity.motionZ);
			fromShape.setMotion(entity, tmpMotion);

			System.out.println(modLength + ":" + modHeight + ":" + modDepth);
			
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

		relX /= modLength;
		relY /= modHeight;
		relZ /= modDepth;

		relX += lowestXTo;
		relY += lowestYTo;
		relZ += lowestZTo;

		if(entity.dimension != this.diallingTo.worldObj.provider.dimensionId)
			entity.travelToDimension(this.diallingTo.worldObj.provider.dimensionId);

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase base = (EntityLivingBase) entity;
			if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null){
				base.setPositionAndUpdate(relX, relY, relZ);
				base.addPotionEffect(new PotionEffect(MineGate.wormholeEffect.id, 80, 0));
			}
		}
		else
			entity.setPosition(relX, relY, relZ);
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
		
		EntityManaBurst burst = new EntityManaBurst(worldObj);
		burst.setLocationAndAngles(((int) chicken.posX) + 0.5, ((int) chicken.posY) + 0.5, ((int) chicken.posZ) + 0.5, 0, 0);
		burst.setColor(Color.RED.getRGB());
		burst.setMana(mana);
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
