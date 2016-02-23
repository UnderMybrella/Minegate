package org.abimon.mods.minecraft.minegate.tileentity;

import org.abimon.mods.minecraft.minegate.IStargateLocationOverride;
import org.abimon.mods.minecraft.minegate.Location;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBookHolder extends TileEntity implements IInventory, IStargateLocationOverride{

	public ItemStack book = null;

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return book;
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if(book == null)
			return null;
		if(book.stackSize <= num){
			book = null;
			return book;
		}
		ItemStack itm = book.copy();

		itm.stackSize = num;
		book.stackSize -= num;

		return itm;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return book;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack item) {
		book = item;
	}

	@Override
	public String getInventoryName() {
		return "Book Pedestal";
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
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack item) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);

		book = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("BookItem"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		NBTTagCompound itemNBT = new NBTTagCompound();
		if(book != null)
			itemNBT = book.writeToNBT(itemNBT);
		nbt.setTag("BookItem", itemNBT);
	}

	@Override
	public Location getLocationOverride(World world, int x, int y, int z) {
		if(book != null)
			if(book.getItem() instanceof ItemLinking){
				ItemLinking bookItem = (ItemLinking) book.getItem();
				ILinkInfo info = bookItem.getLinkInfo(book);

				if(info != null && info.getSpawn() != null)
					if(LinkListenerManager.isLinkPermitted(world, new EntityChicken(world), info))
						return new Location(MinecraftServer.getServer().worldServerForDimension(info.getDimensionUID()), info.getSpawn().posX, info.getSpawn().posY, info.getSpawn().posZ);
			}
		return null;
	}

	@Override
	public int getPriority() {
		return 15;
	}

}
