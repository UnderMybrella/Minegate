package org.abimon.mods.minecraft.minegate.blocks;

import org.abimon.mods.minecraft.minegate.Location;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityHyperDrive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHyperdrive extends BlockContainer {

	public BlockHyperdrive() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityHyperDrive();
	}
	
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	TileEntityHyperDrive drive = (TileEntityHyperDrive) world.getTileEntity(x, y, z);
    	if(player.isSneaking()){
    		System.out.println("Sneaking");
    		drive.index = 0;
    		return true;
    	}
    	
        return true;
    }

}
