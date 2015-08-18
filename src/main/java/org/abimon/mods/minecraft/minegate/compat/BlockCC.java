package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityControl;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCC extends BlockContainer implements INaquadahBlock{

	protected BlockCC() {
		super(Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityControl();
	}

}
