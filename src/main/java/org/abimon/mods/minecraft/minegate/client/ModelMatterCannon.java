package org.abimon.mods.minecraft.minegate.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

/**
 * Matter Cannon - Isaac
 * Created using Tabula 4.1.1
 */
public class ModelMatterCannon extends ModelBase {
    public ModelRenderer Base;
    public ModelRenderer CannonBase1;
    public ModelRenderer CannonBase2;
    public ModelRenderer CannonBase3;
    public ModelRenderer CannonPowerSupply;
    public ModelRenderer CannonBarrel;
    public ModelRenderer CannonBarrelLeft1;
    public ModelRenderer CannonBarrelLeft2;
    public ModelRenderer CannonBarrelLeftConnector;
    public ModelRenderer CannonBarrelRight1;
    public ModelRenderer CannonBarrelUnderconnector;
    public ModelRenderer CanonPowerBlock;
    public ModelRenderer CannonBarrelRightConnector;
    public ModelRenderer CannonBarrelRight2;

    public ModelMatterCannon() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.Base = new ModelRenderer(this, 0, 0);
        this.Base.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.Base.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
        this.CannonBarrelRightConnector = new ModelRenderer(this, 0, 0);
        this.CannonBarrelRightConnector.setRotationPoint(-3.0F, -5.0F, 3.0F);
        this.CannonBarrelRightConnector.addBox(0.0F, 0.0F, 0.0F, 26, 2, 1, 0.0F);
        this.CannonBase3 = new ModelRenderer(this, 0, 0);
        this.CannonBase3.setRotationPoint(-5.0F, 5.0F, -5.0F);
        this.CannonBase3.addBox(0.0F, 0.0F, 0.0F, 10, 1, 10, 0.0F);
        this.CanonPowerBlock = new ModelRenderer(this, 0, 0);
        this.CanonPowerBlock.setRotationPoint(-3.0F, -1.0F, -3.0F);
        this.CanonPowerBlock.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.CannonBarrel = new ModelRenderer(this, 0, 0);
        this.CannonBarrel.setRotationPoint(-4.0F, -7.0F, -3.0F);
        this.CannonBarrel.addBox(0.0F, 0.0F, 0.0F, 28, 6, 6, 0.0F);
        this.CannonPowerSupply = new ModelRenderer(this, 0, 0);
        this.CannonPowerSupply.setRotationPoint(-4.0F, 0.0F, -4.0F);
        this.CannonPowerSupply.addBox(0.0F, 0.0F, 0.0F, 8, 5, 8, 0.0F);
        this.CannonBarrelRight2 = new ModelRenderer(this, 0, 0);
        this.CannonBarrelRight2.setRotationPoint(-3.0F, -6.0F, 5.0F);
        this.CannonBarrelRight2.addBox(0.0F, 0.0F, 0.0F, 26, 4, 1, 0.0F);
        this.CannonBarrelUnderconnector = new ModelRenderer(this, 0, 0);
        this.CannonBarrelUnderconnector.setRotationPoint(3.0F, -1.0F, -1.0F);
        this.CannonBarrelUnderconnector.addBox(0.0F, 0.0F, 0.0F, 21, 1, 2, 0.0F);
        this.CannonBase1 = new ModelRenderer(this, 0, 0);
        this.CannonBase1.setRotationPoint(-7.0F, 7.0F, -7.0F);
        this.CannonBase1.addBox(0.0F, 0.0F, 0.0F, 14, 1, 14, 0.0F);
        this.CannonBase2 = new ModelRenderer(this, 0, 0);
        this.CannonBase2.setRotationPoint(-6.0F, 6.0F, -6.0F);
        this.CannonBase2.addBox(0.0F, 0.0F, 0.0F, 12, 1, 12, 0.0F);
        this.CannonBarrelLeft2 = new ModelRenderer(this, 0, 0);
        this.CannonBarrelLeft2.setRotationPoint(-3.0F, -6.0F, -6.0F);
        this.CannonBarrelLeft2.addBox(0.0F, 0.0F, 0.0F, 26, 4, 1, 0.0F);
        this.CannonBarrelRight1 = new ModelRenderer(this, 0, 0);
        this.CannonBarrelRight1.setRotationPoint(-4.0F, -7.0F, 4.0F);
        this.CannonBarrelRight1.addBox(0.0F, 0.0F, 0.0F, 28, 6, 1, 0.0F);
        this.CannonBarrelLeft1 = new ModelRenderer(this, 0, 0);
        this.CannonBarrelLeft1.setRotationPoint(-4.0F, -7.0F, -5.0F);
        this.CannonBarrelLeft1.addBox(0.0F, 0.0F, 0.0F, 28, 6, 1, 0.0F);
        this.CannonBarrelLeftConnector = new ModelRenderer(this, 0, 0);
        this.CannonBarrelLeftConnector.setRotationPoint(-3.0F, -5.0F, -4.0F);
        this.CannonBarrelLeftConnector.addBox(0.0F, 0.0F, 0.0F, 26, 2, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/blocks/stone.png"));
        this.Base.render(f5);
        this.CannonBarrelRightConnector.render(f5);
        this.CannonBase3.render(f5);
        this.CanonPowerBlock.render(f5);
        this.CannonBarrel.render(f5);
        this.CannonPowerSupply.render(f5);
        this.CannonBarrelRight2.render(f5);
        this.CannonBarrelUnderconnector.render(f5);
        this.CannonBase1.render(f5);
        this.CannonBase2.render(f5);
        this.CannonBarrelLeft2.render(f5);
        this.CannonBarrelRight1.render(f5);
        this.CannonBarrelLeft1.render(f5);
        this.CannonBarrelLeftConnector.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
