package org.abimon.mods.minecraft.minegate.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockNaquadahReactorFake extends Block {

	public BlockNaquadahReactorFake() {
		super(Material.air);
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

}
