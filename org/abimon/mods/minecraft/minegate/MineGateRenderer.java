package org.abimon.mods.minecraft.minegate;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class MineGateRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		if(block == MineGate.glyphBlock)
		{
			renderer.renderStandardBlock(MineGate.stargateFrame, x, y, z);
			renderer.renderStandardBlock(new BlockDisplay(){
				@SideOnly(Side.CLIENT)
				public IIcon getIcon(IBlockAccess access, int x, int y, int z, int p_149673_5_){
					return MineGate.glyphBlock.getIcon(access, x, y, z, p_149673_5_);
				}
			}, x, y, z);
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return MineGate.renderID;
	}

}
