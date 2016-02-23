package org.abimon.mods.minecraft.minegate.blocks;

import org.abimon.mods.minecraft.minegate.tileentity.TileEntityMatterCannon;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMatterCannon extends BlockContainer {

	public BlockMatterCannon() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityMatterCannon();
	}

}
