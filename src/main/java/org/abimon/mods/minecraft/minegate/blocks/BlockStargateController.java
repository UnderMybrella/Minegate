package org.abimon.mods.minecraft.minegate.blocks;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.EnumDirection;
import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

@Interface(iface = "vazkii.botania.api.lexicon.ILexiconable", modid = "Botania")
public class BlockStargateController extends BlockContainer implements ILexiconable, INaquadahBlock{

	Class<? extends TileEntityStargateController> tileEntity = TileEntityStargateController.class;

	public BlockStargateController() {
		this(Material.rock);
	}

	public BlockStargateController(Material mat){
		super(mat);
	}

	public int getMobilityFlag()
	{
		return 2;
	}

	public BlockStargateController(Material mat,  Class<? extends TileEntityStargateController> tileEntity){
		super(mat);
		this.tileEntity = tileEntity;
	}

	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		try{
			return tileEntity.newInstance();
		}
		catch(Throwable th){}
		return new TileEntityStargateController();
	}

	//	public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
	//		TileEntityStargateController controller = (TileEntityStargateController) world.getTileEntity(x, y, z);
	//		if(controller == null)
	//		{
	//			controller = (TileEntityStargateController) createNewTileEntity(world, 0);
	//			world.setTileEntity(x, y, z, controller);
	//		}
	//
	//		controller.checkFrame();
	//	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		try{
			if(!world.isRemote){
				TileEntityStargateController controller = (TileEntityStargateController) world.getTileEntity(x, y, z);
				if(controller == null)
				{
					controller = (TileEntityStargateController) createNewTileEntity(world, 0);
					world.setTileEntity(x, y, z, controller);
				}

				ItemStack item = player.getEquipmentInSlot(0);
				if(item != null && item.getItem() == MineGate.glyphSetter && item.hasTagCompound()){
					int[] glyphs = item.getTagCompound().getIntArray("Glyphs");
					Glyph[] address = new Glyph[glyphs.length];
					for(int i = 0; i < glyphs.length; i++)
						address[i] = MineGate.glyphList.get(glyphs[i]);

					if(MineGate.addressToController.containsKey(address)){
						player.addChatComponentMessage(new ChatComponentText("A Stargate already exists with that address!"));
						return false;
					}
					else
						player.addChatComponentMessage(new ChatComponentText("Registered"));

					MineGate.addressToController.remove(controller.address);

					controller.address = address;					

					MineGate.addToRegister(controller);
					MineGate.reloadNetwork();
					controller.markDirty();
				}
				else if(item != null && item.getItem() == MineGate.dhdBinder && item.hasTagCompound())
				{
					if(!item.hasTagCompound())
						item.setTagCompound(new NBTTagCompound());

					item.getTagCompound().setInteger("BoundX", x);
					item.getTagCompound().setInteger("BoundY", y);
					item.getTagCompound().setInteger("BoundZ", z);
					player.addChatMessage(new ChatComponentText("Bound!"));
				}
				else if(item != null)
				{
					boolean isHammer = false;

					for(Class inter : item.getItem().getClass().getInterfaces())
						if(inter.getName().equals("cofh.api.item.IToolHammer"))
						{
							isHammer = true;
							break;
						}
						else
							System.out.println(inter.getName());

					if(isHammer){

						int counter = 0;
						for(int i = 0; i < EnumDirection.values().length; i++)
							if(EnumDirection.values()[i] == controller.direction)
							{
								counter = i;
								break;
							}

						if(player.isSneaking())
							counter--;
						else
							counter++;

						if(counter == EnumDirection.values().length)
							counter = 0;
						if(counter == -1)
							counter = EnumDirection.values().length - 1;

						controller.direction = EnumDirection.values()[counter];

						player.addChatMessage(new ChatComponentText("Changed Stargate direction to " + controller.direction));

						controller.markDirty();
					}
				}
				else{
					String addr = "";
					if(controller != null && controller.address != null)
						for(Glyph glyph : controller.address)
							addr += glyph.glyphName + ", ";
					if(!addr.equals(""))
						player.addChatMessage(new ChatComponentText("Stargate Address: " + addr.substring(0, addr.length() - 2)));

					controller.checkFrame();

					MineGate.addToRegister(controller);
					MineGate.reloadNetwork();
				}
			}
			else
			{
				TileEntityStargateController controller = (TileEntityStargateController) world.getTileEntity(x, y, z);
				if(controller == null)
				{
					controller = (TileEntityStargateController) createNewTileEntity(world, 0);
					world.setTileEntity(x, y, z, controller);
				}
				
				controller.checkFrame();

				MineGate.addToRegister(controller);
				MineGate.reloadNetwork();
			}
			
			return true;
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return false;
	}

	//	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
	//	{
	//		TileEntityStargateController controller = (TileEntityStargateController) world.getTileEntity(x, y, z);
	//		if(controller == null)
	//			return;
	//
	//		MineGate.registering.add(controller);
	//
	//		MineGate.reloadNetwork();
	//
	//		controller.markDirty();
	//	}

	@Override
	@Method(modid = "Botania")
	public LexiconEntry getEntry(World arg0, int arg1, int arg2, int arg3, EntityPlayer player, ItemStack item) {
		return BotaniaCompatibility.stargate;
	}

	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_){
		TileEntity te = world.getTileEntity(x, y, z);
		System.out.println(te);
	}
}
