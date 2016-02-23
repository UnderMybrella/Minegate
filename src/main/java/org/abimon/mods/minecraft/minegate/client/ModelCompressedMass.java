package org.abimon.mods.minecraft.minegate.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCompressedMass extends ModelBase {
    public ModelRenderer CentralMass;
    public ModelRenderer TopMass;
    public ModelRenderer BottomMass;
    public ModelRenderer LeftMass;
    public ModelRenderer RightMass;
    public ModelRenderer FrontMass;
    public ModelRenderer BackMass;

    public ModelCompressedMass() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.CentralMass = new ModelRenderer(this, 0, 0);
        this.CentralMass.setRotationPoint(-4.0F, 8.0F, -4.0F);
        this.CentralMass.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.BottomMass = new ModelRenderer(this, 0, 0);
        this.BottomMass.setRotationPoint(-3.0F, 16.0F, -3.0F);
        this.BottomMass.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.BackMass = new ModelRenderer(this, 0, 0);
        this.BackMass.setRotationPoint(-5.0F, 9.0F, -3.0F);
        this.BackMass.addBox(0.0F, 0.0F, 0.0F, 1, 6, 6, 0.0F);
        this.LeftMass = new ModelRenderer(this, 0, 0);
        this.LeftMass.setRotationPoint(-3.0F, 9.0F, -5.0F);
        this.LeftMass.addBox(0.0F, 0.0F, 0.0F, 6, 6, 1, 0.0F);
        this.FrontMass = new ModelRenderer(this, 0, 0);
        this.FrontMass.setRotationPoint(4.0F, 9.0F, -3.0F);
        this.FrontMass.addBox(0.0F, 0.0F, 0.0F, 1, 6, 6, 0.0F);
        this.TopMass = new ModelRenderer(this, 0, 0);
        this.TopMass.setRotationPoint(-3.0F, 7.0F, -3.0F);
        this.TopMass.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.RightMass = new ModelRenderer(this, 0, 0);
        this.RightMass.setRotationPoint(-3.0F, 9.0F, 4.0F);
        this.RightMass.addBox(0.0F, 0.0F, 0.0F, 6, 6, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.CentralMass.render(f5);
        this.BottomMass.render(f5);
        this.BackMass.render(f5);
        this.LeftMass.render(f5);
        this.FrontMass.render(f5);
        this.TopMass.render(f5);
        this.RightMass.render(f5);
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
