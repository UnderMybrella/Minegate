package org.abimon.mods.minecraft.minegate.client;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class CompressedMatterRenderer extends Render {
	
	ModelCompressedMass matter;
	
	public CompressedMatterRenderer(){
		matter = new ModelCompressedMass();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5f, (float)y, (float)z + 0.5f);
        this.bindTexture(getEntityTexture(entity));
        float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
        f3 += f3 * f3;
        this.matter.render(entity, 0.0F, f2 * 3.0F, f3 * 0.2F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return new ResourceLocation(MineGate.MODID + ":textures/entity/compressed_matter.png");
	}

}
