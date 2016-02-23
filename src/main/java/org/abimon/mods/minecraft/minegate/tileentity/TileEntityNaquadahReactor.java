package org.abimon.mods.minecraft.minegate.tileentity;

import java.util.Random;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.compat.IC2Compatibility;
import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.naquity.INaquityProvider;
import org.abimon.mods.minecraft.minegate.reactor.IReactorComponentProvider;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahReactorComponent;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;


@InterfaceList(value = {@Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")})
public class TileEntityNaquadahReactor extends TileEntity implements IInventory, INaquityProvider,  IEnergyHandler{

	public NaquadahReactorComponent[] layers = new NaquadahReactorComponent[10];
	public int rotation = 0;
	public float naquity = 0.0f;

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);

		for(int i = 0; i < layers.length; i++){
			NBTTagCompound layer = nbt.getCompoundTag("Layer" + i);
			NBTTagCompound layerNBT = nbt.getCompoundTag("Layer" + i + "NBT");
			ItemStack item = ItemStack.loadItemStackFromNBT(layer);
			if(item != null && item.getItem() instanceof IReactorComponentProvider){
				layers[i] = ((IReactorComponentProvider) item.getItem()).getComponent(item, this);
				layers[i].readFromNBT(layerNBT);
			}
			else
				layers[i] = null;
		}

		rotation = nbt.getInteger("Rotation");
		naquity = nbt.getFloat("Naquity");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);

		for(int i = 0; i < layers.length; i++){
			NBTTagCompound layer = new NBTTagCompound();
			NBTTagCompound layerNBT = new NBTTagCompound();
			if(layers[i] != null)
				if(layers[i].getItemStackForComponent() != null){
					layer = layers[i].getItemStackForComponent().writeToNBT(layer);
					layers[i].writeToNBT(layerNBT);
				}
			nbt.setTag("Layer" + i, layer);
			nbt.setTag("Layer" + i + "NBT", layerNBT);
		}
		nbt.setInteger("Rotation", rotation);
		nbt.setFloat("Naquity", naquity);
	}

	public float getFuel() {
		float naquadahContent = 0.0f;
		for(NaquadahReactorComponent layer : layers)
			if(layer != null)
				naquadahContent += layer.getFuelValue();
		return naquadahContent;
	}

	public float getConsumption(){
		float naquadahConsumption = 0.0001f;
		for(NaquadahReactorComponent layer : layers)
			if(layer != null){
				naquadahConsumption *= layer.getFuelConsumption();
			}
		return naquadahConsumption;
	}

	public float getProduction(){
		float naquityProduced = 0.0025f;
		for(NaquadahReactorComponent layer : layers)
			if(layer != null)
				naquityProduced *= layer.getPowerModifier();
		return naquityProduced;
	}

	@Override
	public void updateEntity(){

		float naquadahContent = getFuel();
		float naquadahConsumption = getConsumption();

		if(naquadahContent > naquadahConsumption){

			naquity += getProduction();

			float naquadahStillRequired = naquadahConsumption;

			for(NaquadahReactorComponent layer : layers)
				if(layer != null)
					naquadahStillRequired = layer.useFuel(naquadahStillRequired);
		}

		for(NaquadahReactorComponent layer : layers)
			if(layer != null)
				layer.onTick();

		for(int i = 0; i < layers.length; i++)
			if(layers[i] != null && layers[i].getItemStackForComponent() == null)
				layers[i] = null;

		if(naquity >= Short.MAX_VALUE){
			if(IC2Compatibility.loaded)
				IC2Compatibility.doNuke(worldObj, xCoord, yCoord, zCoord, new Random().nextInt(15) + 22);
			else
				worldObj.createExplosion(null, xCoord, yCoord, zCoord, 32, true);
		}
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

	public boolean push(NaquadahReactorComponent component){
		for(int i = 0; i < layers.length; i++)
			if(component != null && layers[i] == null){
				layers[i] = component;
				component = null;
			}
		this.markDirty();
		return component == null;
	}

	public NaquadahReactorComponent pop(){
		for(int i = layers.length - 1; i >= 0; i--)
			if(layers[i] != null){
				NaquadahReactorComponent comp = layers[i];
				layers[i] = null;
				this.markDirty();
				return comp;
			}
		return null;
	}

	@Override
	public int getSizeInventory() {
		return layers.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return layers[slot] != null ? layers[slot].getItemStackForComponent() : null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if(layers[slot] == null)
			return null;
		ItemStack itm = layers[slot].getItemStackForComponent();
		layers[slot] = null;
		this.markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		return itm;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(stack == null || !(stack.getItem() instanceof IReactorComponentProvider))
			layers[slot] = null;
		else
			layers[slot] = ((IReactorComponentProvider) stack.getItem()).getComponent(stack, this);
		this.markDirty();
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public String getInventoryName() {
		return MineGate.MODID + ":reactor";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack == null ? true : stack.getItem() instanceof IReactorComponentProvider;
	}

	@Override
	public float naquityContent() {

		float naquityTmp = naquity;

		for(NaquadahReactorComponent comp : layers)
			if(comp != null)
				naquityTmp = Math.min(naquityTmp + comp.getEnergyStored(EnumEnergy.NAQUITY), Float.MAX_VALUE);

		return naquityTmp;
	}

	@Override
	public float retrieveNaquity(float removalService) {
		if(naquity >= removalService){
			naquity -= removalService;
			return removalService;
		}
		else{
			float retrieved = naquity;
			removalService -= naquity;
			naquity = 0;

			for(NaquadahReactorComponent comp : layers)
				if(comp != null)
					if(comp.getEnergyStored(EnumEnergy.NAQUITY) > 0){
						float energyStored = comp.getEnergyStored(EnumEnergy.NAQUITY);
						if(energyStored >= removalService){
							retrieved += comp.retrieveEnergy(EnumEnergy.NAQUITY, removalService);
							removalService = 0;
						}
						else{
							float iRetrieved = comp.retrieveEnergy(EnumEnergy.NAQUITY, energyStored);
							removalService -= iRetrieved;
							retrieved += iRetrieved;
						}
					}

			return retrieved;
		}
	}

	@Override
	@Method(modid = "CoFHCore")
	public boolean canConnectEnergy(ForgeDirection paramForgeDirection) {
		return true;
	}

	@Override
	@Method(modid = "CoFHCore")
	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		int energy = 0;
		for(NaquadahReactorComponent comp : layers)
			if(comp != null)
				energy += comp.getEnergyStored(EnumEnergy.RF);
		return energy;
	}

	@Override
	@Method(modid = "CoFHCore")
	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return getEnergyStored(paramForgeDirection) + 1;
	}

	@Override
	@Method(modid = "CoFHCore")
	public int extractEnergy(ForgeDirection paramForgeDirection, int num, boolean sim) {
		int extracted = 0;

		for(NaquadahReactorComponent comp : layers)
			if(comp != null){
				int stored = (int) comp.getEnergyStored(EnumEnergy.RF);
				if(!sim){
					int retrieved = (int) comp.retrieveEnergy(EnumEnergy.RF, Math.min(num - extracted, stored));
					extracted += retrieved;
				}
				else
					extracted += stored;
			}
		return extracted;
	}

	@Override
	public int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean) {
		return 0;
	}
}
