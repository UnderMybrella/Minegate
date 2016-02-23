package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.entity.EntityCompressedMatter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMatterCannon extends TileEntity implements IInventory {
	
	public ItemStack item = null;
	
	public int index = 0;
	
	public static final ForgeDirection[] ORIENTATIONS = new ForgeDirection[]{ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH};
	
	@Override
	public void updateEntity(){
		if(item != null){
			EntityCompressedMatter matter = new EntityCompressedMatter(worldObj);
			matter.setPosition(xCoord + ORIENTATIONS[index].offsetX * 2, yCoord + 1, zCoord + ORIENTATIONS[index].offsetZ * 2);
			matter.addVelocity(0.5 * ORIENTATIONS[index].offsetX, 0, 0.5 * ORIENTATIONS[index].offsetZ);
			matter.velocityChanged = true;
			worldObj.spawnEntityInWorld(matter);
			item = null;
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return item;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack itm) {
		item = itm;
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

}
