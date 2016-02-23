package org.abimon.mods.minecraft.minegate.blocks;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityBookHolder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBookHolder extends Block implements INaquadahBlock, ITileEntityProvider{

	IIcon filled = null;

	public BlockBookHolder() {
		super(Material.rock);
	}

	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityBookHolder();
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(world.isRemote)
			return false;
		TileEntity te = world.getTileEntity(x, y, z);

		if(te != null && te instanceof TileEntityBookHolder){
			TileEntityBookHolder inv = (TileEntityBookHolder) te;
			
			if(inv.getStackInSlot(0) != null){
				EntityItem item = new EntityItem(world, (int) player.posX, (int) player.posY, (int) player.posZ, inv.getStackInSlot(0));
				world.spawnEntityInWorld(item);
			}

			if(player.getCurrentEquippedItem() != null){
			inv.setInventorySlotContents(0, player.getCurrentEquippedItem().copy());
				player.getCurrentEquippedItem().stackSize = 0;
			}
			else
				inv.setInventorySlotContents(0, null);

			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int p_149673_5_)
	{
		if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof IInventory){
			ItemStack itm = ((IInventory) world.getTileEntity(x, y, z)).getStackInSlot(0);
			if(itm != null)
				return filled;
		}
		return blockIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_)
	{
		this.blockIcon = p_149651_1_.registerIcon(MineGate.MODID + ":bookholderEmpty");
		this.filled = p_149651_1_.registerIcon(MineGate.MODID + ":bookholderFilled");
	}

}
