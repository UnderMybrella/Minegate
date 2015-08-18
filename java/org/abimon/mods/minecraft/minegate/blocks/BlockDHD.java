package org.abimon.mods.minecraft.minegate.blocks;

import java.util.LinkedList;

import org.abimon.mods.minecraft.minegate.Glyph;
import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityChevron;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BlockDHD extends BlockContainer {

	public BlockDHD(Material p_i45386_1_) {
		super(p_i45386_1_);

		//this.setBlockBounds(-1, 0, -1, 1, 2, 1);
		this.setHardness(100);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDHD();
	}

	//You don't want the normal render type, or it wont render properly.
	@Override
	public int getRenderType() {
		return -1;
	}

	//It's not an opaque cube, so you need this.
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	//It's not a normal block, so you need this too.
	public boolean renderAsNormalBlock() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
	{
		return AxisAlignedBB.getBoundingBox((double)p_149633_2_ - 0.5, (double)p_149633_3_, (double)p_149633_4_ - 0.5, (double)p_149633_2_ + 1.5, (double)p_149633_3_ + 1.5, (double)p_149633_4_ + 1.5);
	}  

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	{
		return AxisAlignedBB.getBoundingBox((double)p_149668_2_, (double)p_149668_3_, (double)p_149668_4_, (double)p_149668_2_ + 1, (double)p_149668_3_ + 1.5, (double)p_149668_4_ + 1);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float posX, float posY, float posZ)
	{
		if(world.isRemote)
			return true;
		TileEntityDHD dhd = (TileEntityDHD) world.getTileEntity(x, y, z);

		if(player.getHeldItem() != null)
			if(player.getHeldItem().getItem() == MineGate.dhdBinder)
			{
				NBTTagCompound nbt = player.getHeldItem().getTagCompound();
				if(nbt != null)
				{
					dhd.boundX = nbt.getInteger("BoundX");
					dhd.boundY = nbt.getInteger("BoundY");
					dhd.boundZ = nbt.getInteger("BoundZ");
					player.addChatMessage(new ChatComponentText("Bound to " + dhd.boundX + ", " + dhd.boundY + ", " + dhd.boundZ));
					dhd.markDirty();
					return true;
				}
			}

		Glyph glyphInHand = null;

		if(player.getHeldItem() != null && player.getHeldItem().getItem() == MineGate.glyphs)
			glyphInHand = MineGate.glyphList.get(player.getHeldItem().getItemDamage());

		boolean northHemisphere = posZ > 0.5f;
		boolean eastHemisphere = posX > 0.5f;

		boolean northNorth = posZ > 0.75;
		boolean eastEast = posX > 0.75;
		boolean southSouth = posZ < 0.25;
		boolean westWest = posX < 0.25;

		if(glyphInHand != null){
			if(northHemisphere)
				if(eastHemisphere)
					if(northNorth)
						if(eastEast)
							dhd.glyphs[8] = glyphInHand;
						else
							dhd.glyphs[7] = glyphInHand;
					else
						dhd.glyphs[2] = glyphInHand;
				else
					if(northNorth)
						if(westWest)
							dhd.glyphs[9] = glyphInHand;
						else
							dhd.glyphs[6] = glyphInHand;
					else
						if(westWest)
							dhd.glyphs[1] = glyphInHand;
						else;
			else
				if(eastHemisphere)
					if(southSouth)
						if(eastEast)
							dhd.glyphs[11] = glyphInHand;
						else
							dhd.glyphs[4] = glyphInHand;
					else
						if(eastEast)
							dhd.glyphs[3] = glyphInHand;
						else;
				else
					if(southSouth)
						if(westWest)
							dhd.glyphs[10] = glyphInHand;
						else
							dhd.glyphs[5] = glyphInHand;
					else
						if(westWest)
							dhd.glyphs[0] = glyphInHand;
						else;
			dhd.markDirty();
			world.markBlockForUpdate(x, y, z);
			return true;
		}

		int pressing = -1;

		if(northHemisphere)
			if(eastHemisphere)
				if(northNorth)
					if(eastEast)
						pressing = 8;
					else
						pressing = 7;
				else
					if(eastEast)
						pressing = 2;
					else
						handleButtonPress(world, x, y, z, player);
			else
				if(northNorth)
					if(westWest)
						pressing = 9;
					else
						pressing = 6;
				else
					if(westWest)
						pressing = 1;
					else
						handleButtonPress(world, x, y, z, player);
		else
			if(eastHemisphere)
				if(southSouth)
					if(eastEast)
						pressing = 11;
					else
						pressing = 4;
				else
					if(eastEast)
						pressing = 3;
					else
						handleButtonPress(world, x, y, z, player);
			else
				if(southSouth)
					if(westWest)
						pressing = 10;
					else
						pressing = 5;
				else
					if(westWest)
						pressing = 0;
					else
						handleButtonPress(world, x, y, z, player);

		if(pressing != -1){
			dhd.pressed[pressing] = !player.isSneaking();
			if(player.isSneaking())
				dhd.pressedGlyphs.removeLastOccurrence(dhd.glyphs[pressing]);
			else
				dhd.pressedGlyphs.addLast(dhd.glyphs[pressing]);
		}
		world.markBlockForUpdate(x, y, z);
		return false;
	}

	public void handleButtonPress(World world, int x, int y, int z, EntityPlayer player){
		TileEntityDHD dhd = (TileEntityDHD) world.getTileEntity(x, y, z);
		try{
			player.addChatMessage(new ChatComponentText("Attempting to dialling " + dhd.pressedGlyphs));
			TileEntityStargateController sgc = (TileEntityStargateController) world.getTileEntity(dhd.boundX, dhd.boundY, dhd.boundZ);
			sgc.checkFrame();
			int glyphLoc = 0;
			LinkedList<Glyph> glyphs = new LinkedList<Glyph>();
			glyphs.addAll(dhd.pressedGlyphs);
			for(Location loc : sgc.frameBlocks)
				if(sgc.getWorldObj().getBlock(loc.x, loc.y, loc.z) == MineGate.chevron)
				{
					try{
						TileEntityChevron chev = ((TileEntityChevron) sgc.getWorldObj().getTileEntity(loc.x, loc.y, loc.z));
						chev.glyph = dhd.pressedGlyphs.poll();
						chev.glyphLocation = glyphLoc;
						glyphLoc++;
					}
					catch(Throwable th){}
				}
			if(sgc.address == null)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Dialling failed - Dialling stargate has no address"));
				dhd.pressedGlyphs.clear();
				dhd.pressedGlyphs.addAll(glyphs);
			}
			else
				if(sgc.naquityStored >= 10.0f){
					if(sgc.dialOut())
					{
						player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Dialled successfully"));
						for(int i = 0; i < dhd.pressed.length; i++)
							dhd.pressed[i] = false;
					}
					else
					{
						player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Dialling failed - No Stargates with that address"));
						dhd.pressedGlyphs.clear();
						dhd.pressedGlyphs.addAll(glyphs);
					}
				}
				else
				{
					player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Dialling failed - Not enough naquity to initiate wormhole"));
					dhd.pressedGlyphs.clear();
					dhd.pressedGlyphs.addAll(glyphs);
				}
		}
		catch(Throwable th){
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Dialling failed - " + th));
			th.printStackTrace();
		}
	}

	public String handleButtonPress(World world, int x, int y, int z){
		TileEntityDHD dhd = (TileEntityDHD) world.getTileEntity(x, y, z);

		try{
			TileEntityStargateController sgc = (TileEntityStargateController) world.getTileEntity(dhd.boundX, dhd.boundY, dhd.boundZ);
			sgc.checkFrame();
			int glyphLoc = 0;
			LinkedList<Glyph> glyphs = new LinkedList<Glyph>();
			glyphs.addAll(dhd.pressedGlyphs);
			for(Location loc : sgc.frameBlocks)
				if(sgc.getWorldObj().getBlock(loc.x, loc.y, loc.z) == MineGate.chevron)
				{
					try{
						TileEntityChevron chev = ((TileEntityChevron) sgc.getWorldObj().getTileEntity(loc.x, loc.y, loc.z));
						chev.glyph = dhd.pressedGlyphs.poll();
						chev.glyphLocation = glyphLoc;
						glyphLoc++;
					}
					catch(Throwable th){}
				}

			if(sgc.address == null){
				dhd.pressedGlyphs.clear();
				dhd.pressedGlyphs.addAll(glyphs);
				return "Dialling stargate has no address";
			}
			else
				if(sgc.naquityStored >= 10.0f)
					if(sgc.dialOut())
					{					for(int i = 0; i < dhd.pressed.length; i++)
						dhd.pressed[i] = false;
					return null;
					}
					else;
				else
				{
					dhd.pressedGlyphs.clear();
					dhd.pressedGlyphs.addAll(glyphs);
					return "Not enough naquity stored to initiate wormhole";
				}
			dhd.pressedGlyphs.clear();
			dhd.pressedGlyphs.addAll(glyphs);
			return "No Stargates with that address";
		}
		catch(Throwable th){
			return th.toString();
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
	{

	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.setBlock(x+1, y, z, MineGate.dhdGhost, 0, 3);
		world.setBlock(x+1, y, z+1, MineGate.dhdGhost, 1, 3);
		world.setBlock(x, y, z+1, MineGate.dhdGhost, 2, 3);
		world.setBlock(x-1, y, z+1, MineGate.dhdGhost, 3, 3);
		world.setBlock(x-1, y, z, MineGate.dhdGhost, 4, 3);
		world.setBlock(x-1, y, z-1, MineGate.dhdGhost, 5, 3);
		world.setBlock(x, y, z-1, MineGate.dhdGhost, 6, 3);
		world.setBlock(x+1, y, z-1, MineGate.dhdGhost, 7, 3);
	}

	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_){
		super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);

		world.setBlock(x+1, y, z, Blocks.air, 0, 3);
		world.setBlock(x+1, y, z+1, Blocks.air, 0, 3);
		world.setBlock(x, y, z+1, Blocks.air, 0, 3);
		world.setBlock(x-1, y, z+1, Blocks.air, 0, 3);
		world.setBlock(x-1, y, z, Blocks.air, 0, 3);
		world.setBlock(x-1, y, z-1, Blocks.air, 0, 3);
		world.setBlock(x, y, z-1, Blocks.air, 0, 3);
		world.setBlock(x+1, y, z-1, Blocks.air, 0, 3);

		world.setBlock(x, y, z, Blocks.air, 0, 3);
	}

}
