package org.abimon.mods.minecraft.minegate.blocks;

import java.util.Random;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityChevron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockChevron extends BlockContainer implements INaquadahBlock{

	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return false;
	}

	public BlockChevron() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityChevron();
	}

	//    public boolean onBlockEventReceived(World world, int x, int y, int z, int p_149696_5_, int p_149696_6_)
	//    {
	//    	if (!world.isRemote)
	//		{
	//			boolean flag = this.isIndirectlyPowered(world, x, y, z);
	//			if(flag && world.getTileEntity(x, y, z) instanceof TileEntityChevron)
	//			{
	//				TileEntityChevron chevron = (TileEntityChevron) world.getTileEntity(x, y, z);
	//				if(chevron.glyph != null)
	//					for(int i = 0; i < MineGate.glyphList.size(); i++)
	//						if(MineGate.glyphList.get(i).equals(chevron.glyph))
	//						{
	//							i++;
	//							if(i == MineGate.glyphList.size())
	//								chevron.glyph = null;
	//							else
	//								chevron.glyph = MineGate.glyphList.get(i);
	//							break;
	//						}
	//						else;
	//				else
	//					chevron.glyph = MineGate.glyphList.getFirst();
	//				chevron.markDirty();
	//			}
	//		}
	//    	return true;
	//    }

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		try{
			TileEntityChevron glyphTE = (TileEntityChevron) world.getTileEntity(x, y, z);
			if(player.getEquipmentInSlot(0) == null)
				if(player.isSneaking())
					glyphTE.glyphLocation--;
				else 
					glyphTE.glyphLocation++;
			else if(player.getEquipmentInSlot(0).getItem() == MineGate.glyphs){
				if(glyphTE.glyph == null)
					glyphTE.glyph = MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage());
				if(!glyphTE.glyph.equals(MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage())))
					for(int i = 0; i < MineGate.glyphList.size(); i++)
						if(MineGate.glyphList.get(i).equals(glyphTE.glyph))
						{
							glyphTE.glyph = MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage());
							break;
						}
						else;
			}
			
			glyphTE.glyphLocation = Math.max(0, glyphTE.glyphLocation);
				
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return false;
	}

	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			if (world.isBlockIndirectlyGettingPowered(x, y, z))
				doChange(world, x, y, z);
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor Block
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
	{
		if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityChevron)
		{
			TileEntityChevron chevron = (TileEntityChevron) world.getTileEntity(x, y, z);
			if (world.getBlockPowerInput(x, y, z) == 15 && chevron.prevPower != 15)
			{
				doChange(world, x, y, z);
			}
			chevron.prevPower = world.getBlockPowerInput(x, y, z);
			chevron.markDirty();
		}
	}

	public void doChange(World world, int x, int y, int z){
		if (!world.isRemote)
		{
			TileEntityChevron chevron = (TileEntityChevron) world.getTileEntity(x, y, z);
			if(chevron.glyph != null)
				for(int i = 0; i < MineGate.glyphList.size(); i++)
					if(MineGate.glyphList.get(i).equals(chevron.glyph))
					{
						i++;
						if(i == MineGate.glyphList.size())
							chevron.glyph = null;
						else
							chevron.glyph = MineGate.glyphList.get(i);
						break;
					}
					else;
			else
				chevron.glyph = MineGate.glyphList.getFirst();
			chevron.markDirty();
		}
	}

	public boolean isOpaqueCube()
	{
		return true;
	}

	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	/**
	 * Checks if the block is a solid face on the given side, used by placement logic.
	 *
	 * @param world The current world
	 * @param x X Position
	 * @param y Y position
	 * @param z Z position
	 * @param side The side to check
	 * @return True if the block is solid on the specified side.
	 */
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}
}
