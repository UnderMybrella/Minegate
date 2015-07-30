package org.abimon.mods.minecraft.minegate;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockStargatePortal extends BlockContainer {

	Class<? extends TileEntityStargatePortal> portalClass = TileEntityStargatePortal.class;

	public BlockStargatePortal(Material material) {
		super(material);
	}

	public BlockStargatePortal(){
		super(Material.portal);
	}

	public BlockStargatePortal(Class<? extends TileEntityStargatePortal> portalClass, Material material) {
		super(material);
	}

	public BlockStargatePortal(Class<? extends TileEntityStargatePortal> portalClass){
		super(Material.portal);
	}


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
		if(meta != 0)
			return null;
		try{
			return portalClass.newInstance();
		}
		catch(Throwable th){}
		return new TileEntityStargatePortal();
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	{
		return null;//AxisAlignedBB.getBoundingBox((double)p_149668_2_ + this.minX, (double)p_149668_3_ + this.minY, (double)p_149668_4_ + this.minZ, (double)p_149668_2_ + this.maxX, (double)p_149668_3_ + this.maxY, (double)p_149668_4_ + this.maxZ);
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(!world.isRemote)
			if(((int) entity.posX == x || (int) entity.posX == x+1 || (int) entity.posX == x-1) && ((int) entity.posY == y || (int) entity.posY == y+1 || (int) entity.posY == y-1) && ((int) entity.posZ == z || (int) entity.posZ == z+1 || (int) entity.posZ == z-1)){

				if(world.getBlockMetadata(x, y, z) != 0)
				{
					if(entity instanceof EntityLivingBase)
						if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
							MineGate.vaporiseEntity(entity);
						else;
					else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
						MineGate.vaporiseEntity(entity);
					return;
				}

				TileEntityStargatePortal portal = (TileEntityStargatePortal) world.getTileEntity(x, y, z);

				if(portal.diallingTo == null)
				{
					if(entity instanceof EntityLivingBase)
						if(((EntityLivingBase) entity).getActivePotionEffect(MineGate.wormholeEffect) == null)
							MineGate.vaporiseEntity(entity);
						else;
					else if(!entity.getClass().equals("vazkii.botania.common.entity.EntityManaBurst"))
						MineGate.vaporiseEntity(entity);
					return;
				}

				portal.teleportEntity(entity);
			}
	}

}
