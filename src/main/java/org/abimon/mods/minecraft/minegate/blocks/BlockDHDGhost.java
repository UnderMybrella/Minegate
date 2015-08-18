package org.abimon.mods.minecraft.minegate.blocks;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILaputaImmobile;

@InterfaceList(value = {@Interface(iface = "vazkii.botania.api.ILaputaImmobile", modid = "Botania"), @Interface(iface = "vazkii.botania.api.lexicon.ILexiconable", modid = "Botania")})
public class BlockDHDGhost extends Block implements ILaputaImmobile, ILexiconable{

	public BlockDHDGhost(Material p_i45394_1_) {
		super(p_i45394_1_);
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
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return getCollisionBoundingBoxFromPool(world, x, y, z);
	}  

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0)
			return AxisAlignedBB.getBoundingBox(x - 1.5, y, z - 0.5, x + 0.5, y + 1.5, z + 1.5);
		if(meta == 1)
			return AxisAlignedBB.getBoundingBox(x - 1.5, y, z - 1.5, x + 0.5, y + 1.5, z + 0.5);
		if(meta == 2)
			return AxisAlignedBB.getBoundingBox(x - 0.5, y, z - 1.5, x + 1.5, y + 1.5, z + 0.5);
		if(meta == 3)
			return AxisAlignedBB.getBoundingBox(x + 0.5, y, z - 1.5, x + 2.5, y + 1.5, z + 0.5);
		if(meta == 4)
			return AxisAlignedBB.getBoundingBox(x + 0.5, y, z - 0.5, x + 2.5, y + 1.5, z + 1.5);
		if(meta == 5)
			return AxisAlignedBB.getBoundingBox(x + 0.5, y, z + 0.5, x + 2.5, y + 1.5, z + 2.5);
		if(meta == 6)
			return AxisAlignedBB.getBoundingBox(x - 0.5, y, z + 0.5, x + 1.5, y + 1.5, z + 2.5);
		if(meta == 7)
			return AxisAlignedBB.getBoundingBox(x - 1.5, y, z + 0.5, x + 0.5, y + 1.5, z + 2.5);
		return AxisAlignedBB.getBoundingBox(x, y, z, x + 0.5, y + 1, z + 1);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float posX, float posY, float posZ)
	{
		System.out.println(posX + ":" + posY + ":" + posZ + ":" + x + ":" + y + ":" + z);
		return false;
	}
	
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int meta){
		System.out.println("Breaking with " + x + ":" + y + ":" + z);
		if(meta == 0)
			x--;
		if(meta == 1)
		{
			x--;
			z--;
		}
		if(meta == 2)
			z--;
		if(meta == 3)
		{
			x++;
			z--;
		}
		if(meta == 4)
			x++;
		if(meta == 5)
		{
			x++;
			z++;
		}
		if(meta == 6)
			z++;
		if(meta == 7)
		{
			x--;
			z++;
		}
		
		System.out.println("Breaking " + x + ":" + y + ":" + z + ":" + world.getBlock(x, y, z));
		world.getBlock(x, y, z).breakBlock(world, x, y, z, world.getBlock(x, y, z), 0);
	}

	@Override
	public boolean canMove(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return null;
	}

}
