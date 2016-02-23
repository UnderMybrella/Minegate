package org.abimon.mods.minecraft.minegate.blocks;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityControl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRedstoneControl extends BlockContainer implements INaquadahBlock{

	public BlockRedstoneControl(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityControl();
	}
	
    public int getMobilityFlag()
    {
    	return 2;
    }
	
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
    	return true;
    }
    
    private boolean isIndirectlyPowered(World p_150072_1_, int p_150072_2_, int p_150072_3_, int p_150072_4_)
    {
        return p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ - 1, p_150072_4_, 0) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_, 1) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ - 1, 2) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ + 1, 3) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_, p_150072_4_, 5) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_, p_150072_4_, 4) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_, 0) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 2, p_150072_4_, 1) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ - 1, 2) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ + 1, 3) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_ + 1, p_150072_4_, 4) ? true : p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_ + 1, p_150072_4_, 5)))))))))));
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int p_149696_5_, int p_149696_6_)
    {
        if (!world.isRemote)
        {
            boolean flag = this.isIndirectlyPowered(world, x, y, z);
            if(flag)
            {
            	return false;
            }
        }
        return true;
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
    {
    	if (!world.isRemote)
        {
            boolean flag = this.isIndirectlyPowered(world, x, y, z);
            if(flag && world.getTileEntity(x, y, z) instanceof TileEntityControl)
            {
            	TileEntityControl control = (TileEntityControl) world.getTileEntity(x, y, z);
            	int meta = world.getBlockMetadata(x, y, z);
            	if(meta == 0)
            		control.disengage = true;
            	control.markDirty();
            }
        }
    }
    
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }
}
