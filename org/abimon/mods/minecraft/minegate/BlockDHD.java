package org.abimon.mods.minecraft.minegate;

import java.util.LinkedList;

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

	protected BlockDHD(Material p_i45386_1_) {
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
		return AxisAlignedBB.getBoundingBox((double)p_149633_2_ - 0.5, (double)p_149633_3_, (double)p_149633_4_ - 0.5, (double)p_149633_2_ + 1.5, (double)p_149633_3_ + 1.25, (double)p_149633_4_ + 1.5);
	}  

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	{
		return AxisAlignedBB.getBoundingBox((double)p_149668_2_, (double)p_149668_3_, (double)p_149668_4_, (double)p_149668_2_ + 1, (double)p_149668_3_ + 1.25, (double)p_149668_4_);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float posX, float posY, float posZ)
	{
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
			return true;
		}

		if(northHemisphere)
			if(eastHemisphere)
				if(northNorth)
					if(eastEast)
					{
						player.addChatComponentMessage(new ChatComponentText("Corner 1 - " + dhd.glyphs[8]));
						dhd.pressed[8] = !player.isSneaking();
					}
					else
					{
						player.addChatComponentMessage(new ChatComponentText("Back 2 - " + dhd.glyphs[7]));
						dhd.pressed[7] = !player.isSneaking();
					}
				else
					if(eastEast){
						player.addChatMessage(new ChatComponentText("Left 1 - " + dhd.glyphs[2]));
						dhd.pressed[2] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Button"));
						handleButtonPress(world, x, y, z, player);
					}
			else
				if(northNorth)
					if(westWest)
					{
						player.addChatMessage(new ChatComponentText("Corner 2 - " + dhd.glyphs[9]));
						dhd.pressed[9] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Back 1 - " + dhd.glyphs[6]));
						dhd.pressed[6] = !player.isSneaking();
					}
				else
					if(westWest)
					{
						player.addChatMessage(new ChatComponentText("Right 2 - " + dhd.glyphs[1]));
						dhd.pressed[1] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Button"));
						handleButtonPress(world, x, y, z, player);
					}
		else
			if(eastHemisphere)
				if(southSouth)
					if(eastEast)
					{
						player.addChatMessage(new ChatComponentText("Corner 4 - " + dhd.glyphs[11]));
						dhd.pressed[11] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Front 1 - " + dhd.glyphs[4]));
						dhd.pressed[4] = !player.isSneaking();
					}
				else
					if(eastEast)
					{
						player.addChatMessage(new ChatComponentText("Left 2 - " + dhd.glyphs[3]));
						dhd.pressed[3] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Button"));
						handleButtonPress(world, x, y, z, player);
					}
			else
				if(southSouth)
					if(westWest)
					{
						player.addChatMessage(new ChatComponentText("Corner 3 - " + dhd.glyphs[10]));
						dhd.pressed[10] = !player.isSneaking();
					}
					else{
						player.addChatMessage(new ChatComponentText("Front 2 - " + dhd.glyphs[5]));
						dhd.pressed[5] = !player.isSneaking();
					}
				else
					if(westWest)
					{
						player.addChatComponentMessage(new ChatComponentText("Right 1 - " + dhd.glyphs[0]));
						dhd.pressed[0] = !player.isSneaking();
					}
					else
					{
						player.addChatMessage(new ChatComponentText("Button"));
						handleButtonPress(world, x, y, z, player);
					}
		return false;
	}

	public void handleButtonPress(World world, int x, int y, int z, EntityPlayer player){
		LinkedList<Glyph> pressedGlyphs = new LinkedList<Glyph>();

		TileEntityDHD dhd = (TileEntityDHD) world.getTileEntity(x, y, z);

		for(int i = 0; i < dhd.pressed.length; i++)
			if(dhd.pressed[i])
			{
				pressedGlyphs.add(dhd.glyphs[i]);
				dhd.pressed[i] = false;
			}

		try{
			player.addChatMessage(new ChatComponentText("Attempting to dialling " + pressedGlyphs));
			TileEntityStargateController sgc = (TileEntityStargateController) world.getTileEntity(dhd.boundX, dhd.boundY, dhd.boundZ);
			sgc.checkFrame();
			for(Location loc : sgc.frameBlocks)
				if(sgc.getWorldObj().getBlock(loc.x, loc.y, loc.z) == MineGate.chevron)
				{
					try{
						((TileEntityChevron) sgc.getWorldObj().getTileEntity(loc.x, loc.y, loc.z)).glyph = pressedGlyphs.poll();
					}
					catch(Throwable th){}
				}
			sgc.dialOut();
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Dialled successfully"));
		}
		catch(Throwable th){
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Dialling failed - " + th));
			th.printStackTrace();
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
