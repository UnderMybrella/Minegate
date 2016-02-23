package org.abimon.mods.minecraft.minegate.blocks;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityGlyphs;

import cpw.mods.fml.common.Optional.Interface;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

@Interface(iface = "vazkii.botania.api.lexicon.ILexiconable", modid = "Botania")
public class BlockStargateFrame extends Block implements INaquadahBlock, ILexiconable
{

	public BlockStargateFrame(Material arg0) {
		super(arg0);
	}
	
	/** Disabled for now, will be reimplemented eventually
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if(player.getEquipmentInSlot(0) != null && player.getEquipmentInSlot(0).getItem() == MineGate.glyphs)
		{
			world.setBlock(x, y, z, MineGate.glyphBlock);
			TileEntityGlyphs glyphs = new TileEntityGlyphs();
			glyphs.glyphName = MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage()).glyphName;
			world.setTileEntity(x, y, z, glyphs);
			if(!player.capabilities.isCreativeMode)
				player.getEquipmentInSlot(0).stackSize--;
			return true;
		}
		return false;
	}
	*/
	
    public int getMobilityFlag()
    {
    	return 2;
    }

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return BotaniaCompatibility.stargate;
	}
	
}
