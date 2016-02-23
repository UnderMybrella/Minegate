package org.abimon.mods.minecraft.minegate.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockDisplay extends Block {

	public BlockDisplay(Material material) {
		super(material);
	}

	public BlockDisplay(){
		super(Material.rock);
	}
	
    public int getMobilityFlag()
    {
    	return 2;
    }

}
