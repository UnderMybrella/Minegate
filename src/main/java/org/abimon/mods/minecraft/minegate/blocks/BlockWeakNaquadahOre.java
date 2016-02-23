package org.abimon.mods.minecraft.minegate.blocks;

import java.util.Random;

import org.abimon.mods.minecraft.minegate.MineGate;

import net.minecraft.block.BlockOre;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockWeakNaquadahOre extends BlockOre {
	
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return MineGate.naquadahDerivatives;
	}
	
    public int damageDropped(int p_149692_1_)
    {
        return 1;
    }
    
    public int getMobilityFlag()
    {
    	return 2;
    }
}
