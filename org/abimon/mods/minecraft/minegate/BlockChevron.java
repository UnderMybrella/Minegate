package org.abimon.mods.minecraft.minegate;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChevron extends BlockContainer {

	protected BlockChevron() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityChevron();
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		try{
			TileEntityChevron glyphTE = (TileEntityChevron) world.getTileEntity(x, y, z);
			if(glyphTE.glyph == null)
				glyphTE.glyph = MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage());
			if(!glyphTE.glyph.equals(MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage())))
				for(int i = 0; i < MineGate.glyphList.size(); i++)
					if(MineGate.glyphList.get(i).equals(glyphTE.glyph))
					{
						if(!world.isRemote && !player.capabilities.isCreativeMode){
							EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(MineGate.glyphs, 1, i));
							world.spawnEntityInWorld(item);
						}
						glyphTE.glyph = MineGate.glyphList.get(player.getEquipmentInSlot(0).getItemDamage());
						if(!player.capabilities.isCreativeMode)
							player.getEquipmentInSlot(0).stackSize--;
						break;
					}
					else;
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return false;
	}

}
