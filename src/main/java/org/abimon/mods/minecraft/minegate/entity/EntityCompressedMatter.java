package org.abimon.mods.minecraft.minegate.entity;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargatePortal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCompressedMatter extends EntityThrowable {

	public double origMotionX = 0;
	public double origMotionZ = 0;

	public EntityCompressedMatter(World p_i1582_1_) {
		super(p_i1582_1_);
	}

	public void onEntityUpdate(){
		super.onEntityUpdate();
		if(origMotionX == 0)
			origMotionX = motionX;
		if(origMotionX != 0)
			this.motionX = origMotionX;
		motionY = -0.001;
		if(origMotionZ == 0)
			origMotionZ = motionZ;
		if(origMotionZ != 0)
			this.motionZ = origMotionZ;
		if(this.ticksExisted / 20 > 60)
			if(!this.worldObj.isRemote)
				this.setDead();
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {

		if(mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
			if(worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) instanceof INaquadahBlock)
				return;
			else if(worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) instanceof BlockStargatePortal){
				worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ).onEntityCollidedWithBlock(worldObj, mop.blockX, mop.blockY, mop.blockZ, this);
				return;
			}
		}
		if (!this.worldObj.isRemote){
			if(mop.entityHit != null)
				mop.entityHit.attackEntityFrom(DamageSource.generic, 4);

			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2, true);

			this.setDead();
		}
	}

	public void addVelocity(double motionX, double motionY, double motionZ){
		super.addVelocity(motionX, motionY, motionZ);

		this.origMotionX = motionX;
		this.origMotionZ = motionZ;
	}

}
