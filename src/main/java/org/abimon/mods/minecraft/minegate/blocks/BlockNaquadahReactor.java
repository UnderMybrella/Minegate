package org.abimon.mods.minecraft.minegate.blocks;

import org.abimon.mods.minecraft.minegate.reactor.IReactorComponentProvider;
import org.abimon.mods.minecraft.minegate.reactor.NaquadahReactorComponent;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockNaquadahReactor extends Block implements ITileEntityProvider{

	public BlockNaquadahReactor() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityNaquadahReactor();
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

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		TileEntity teTest = world.getTileEntity(x, y, z);

		if(teTest instanceof TileEntityNaquadahReactor){
			TileEntityNaquadahReactor te = (TileEntityNaquadahReactor) teTest;

			ItemStack holding = player.getCurrentEquippedItem();
			if(player.isSneaking()){
				NaquadahReactorComponent comp = te.pop();
				if(comp != null && !world.isRemote && !player.capabilities.isCreativeMode){
					ItemStack returned = comp.getItemStackForComponent();
					if(!player.inventory.addItemStackToInventory(returned)){
						EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, returned);
						item.delayBeforeCanPickup = 20;
						world.spawnEntityInWorld(item);
						item.motionX = 0;
						item.motionY = 0;
						item.motionZ = 0;
					}
				}
			}
			else if(holding != null && holding.getItem() instanceof IReactorComponentProvider){
				if(te.push(((IReactorComponentProvider) holding.getItem()).getComponent(holding, te)))
					if(!player.capabilities.isCreativeMode)
						holding.stackSize--;
			}

		}

		return false;
	}


}
