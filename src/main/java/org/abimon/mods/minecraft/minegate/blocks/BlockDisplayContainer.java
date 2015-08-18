package org.abimon.mods.minecraft.minegate.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockDisplayContainer extends BlockDisplay implements ITileEntityProvider {

	public abstract TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_);

}
