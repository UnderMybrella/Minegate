package org.abimon.mods.minecraft.minegate.blocks;

import java.util.Random;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityFakePortal;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargatePortal;

import cpw.mods.fml.common.Optional.Interface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

@Interface(iface = "vazkii.botania.api.lexicon.ILexiconable", modid = "Botania")
public class BlockStargatePortal extends BlockContainer implements ILexiconable{

	Class<? extends TileEntityStargatePortal> portalClass = TileEntityStargatePortal.class;

	public BlockStargatePortal(Material material) {
		super(material);
	}

	public BlockStargatePortal(){
		super(Material.portal);
	}
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		return false;
	}

	public BlockStargatePortal(Class<? extends TileEntityStargatePortal> portalClass, Material material) {
		super(material);
	}

	public BlockStargatePortal(Class<? extends TileEntityStargatePortal> portalClass){
		super(Material.portal);
	}

	public int getMobilityFlag()
	{
		return 2;
	}


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
		if(meta != 0)
			return new TileEntityFakePortal();
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
		if(!world.isRemote){
			System.out.println(entity);
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

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return BotaniaCompatibility.stargate;
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {

		if(world.isRemote)
			return;

		if(world.getBlockMetadata(x, y, z) != 0)
			return;

		int liquidX = x;
		int liquidY = y;
		int liquidZ = z;

		if(world.getBlock(x+1, y, z) instanceof IFluidBlock)
			if(((IFluidBlock) world.getBlock(x+1, y, z)).canDrain(world, x+1, y, z))
			{
				liquidX = x+1;
				liquidY = y;
				liquidZ = z;
			}
		if(world.getBlock(x-1, y, z) instanceof IFluidBlock)
			if(((IFluidBlock) world.getBlock(x-1, y, z)).canDrain(world, x-1, y, z))
			{
				liquidX = x-1;
				liquidY = y;
				liquidZ = z;
			}

		if(world.getBlock(x, y, z+1) instanceof IFluidBlock)
			if(((IFluidBlock) world.getBlock(x, y, z+1)).canDrain(world, x, y, z+1))
			{
				liquidX = x;
				liquidY = y;
				liquidZ = z+1;
			}
		if(world.getBlock(x, y, z-1) instanceof IFluidBlock)
			if(((IFluidBlock) world.getBlock(x, y, z-1)).canDrain(world, x, y, z-1))
			{
				liquidX = x;
				liquidY = y;
				liquidZ = z-1;
			}

		if(world.getBlock(x+1, y, z) instanceof BlockLiquid)
		{
			liquidX = x+1;
			liquidY = y;
			liquidZ = z;
		}
		if(world.getBlock(x-1, y, z) instanceof BlockLiquid)
		{
			liquidX = x-1;
			liquidY = y;
			liquidZ = z;
		}

		if(world.getBlock(x, y, z+1) instanceof BlockLiquid)
		{
			liquidX = x;
			liquidY = y;
			liquidZ = z+1;
		}
		if(world.getBlock(x, y, z-1) instanceof BlockLiquid)
		{
			liquidX = x;
			liquidY = y;
			liquidZ = z-1;
		}

		Fluid fluid = null;
		Block fluidBlock = world.getBlock(liquidX, liquidY, liquidZ);

		if(fluidBlock instanceof IFluidBlock)
		{
			FluidStack stack = ((IFluidBlock) fluidBlock).drain(world, liquidX, liquidY, liquidZ, true);
			if(stack != null && stack.getFluid() != null)
				fluid = stack.getFluid();
		}
		else if(fluidBlock instanceof BlockLiquid)
		{
			fluid = FluidRegistry.lookupFluidForBlock(fluidBlock);
			if(fluid == null)
				if(world.getBlockMetadata(liquidX, liquidY, liquidZ) == 0)
					if(fluidBlock.getUnlocalizedName().equalsIgnoreCase("water"))
						fluid = FluidRegistry.WATER;
					else if(fluidBlock.getUnlocalizedName().equalsIgnoreCase("water"))
						fluid = FluidRegistry.LAVA;
		}

		if(fluid == null)
			return;

		if(world.getBlock(liquidX, liquidY, liquidZ) instanceof BlockLiquid)
			world.setBlock(liquidX, liquidY, liquidZ, Blocks.air);

		Entity entity = new EntityChicken(world);
		entity.setPosition(liquidX, liquidY, liquidZ);

		TileEntityStargatePortal portal = (TileEntityStargatePortal) world.getTileEntity(x, y, z);

		portal.teleportEntity(entity);

		world.setBlock((int) entity.posX, (int) entity.posY, (int) entity.posZ, fluid.getBlock());
		world.markBlockForUpdate(liquidX, liquidY, liquidZ);
		world.markBlockForUpdate((int) entity.posX, (int) entity.posY, (int) entity.posZ);
	}


}
