package org.abimon.mods.minecraft.minegate.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.lwjgl.opengl.GL11;

/**
 * Naquadah Reactor - Undefined
 * Created using Tabula 4.1.1
 */
public class ModelNaquadahReactor extends ModelBase {
	public ModelRenderer Base;
	public ModelRenderer[] LAYERS = new ModelRenderer[10];
	public ResourceLocation[] LAYER_LOCATIONS = new ResourceLocation[10]; 
	public ModelRenderer CasePosX;
	public ModelRenderer CaseNegX;
	public ModelRenderer NaquadahSupplyOrb1;
	public ModelRenderer NaquadahSupplyOrb1_1;
	public ModelRenderer NaquadahPowerSupply;
	public ModelRenderer NaquadahPowerSupply_1;
	public ModelRenderer CasePosZ;
	public ModelRenderer CaseNegZ;
	public ModelRenderer Lid;

	public ModelNaquadahReactor() {
		this.textureWidth = 10;
		this.textureHeight = 2;

		this.CaseNegZ = new ModelRenderer(this, 0, 0);
		this.CaseNegZ.setRotationPoint(-8.0F, 7.9F, -8.0F);
		this.CaseNegZ.addBox(0.0F, 0.0F, 0.0F, 16, 16, 1, 0.0F);
		this.CaseNegX = new ModelRenderer(this, 0, 0);
		this.CaseNegX.setRotationPoint(-7.9F, 7.9F, -8.0F);
		this.CaseNegX.addBox(0.0F, 0.0F, 0.0F, 1, 16, 16, 0.0F);

		this.CasePosX = new ModelRenderer(this, 0, 0);
		this.CasePosX.setRotationPoint(7.9F, 7.9F, -8.0F);
		this.CasePosX.addBox(0.0F, 0.0F, 0.0F, 1, 16, 16, 0.0F);
		this.CasePosZ = new ModelRenderer(this, 0, 0);
		this.CasePosZ.setRotationPoint(-8.0F, 7.9F, 8.0F);
		this.CasePosZ.addBox(0.0F, 0.0F, 0.0F, 16, 16, 1, 0.0F);

		this.NaquadahPowerSupply = new ModelRenderer(this, 0, 0);
		this.NaquadahPowerSupply.setRotationPoint(-2.1F, 14.0F, 8.0F);
		this.NaquadahPowerSupply.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F);
		this.NaquadahPowerSupply_1 = new ModelRenderer(this, 0, 0);
		this.NaquadahPowerSupply_1.setRotationPoint(-2.1F, 14.0F, -12.0F);
		this.NaquadahPowerSupply_1.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F);

		this.NaquadahSupplyOrb1 = new ModelRenderer(this, 0, 0);
		this.NaquadahSupplyOrb1.setRotationPoint(-4.1F, 12.0F, 12.0F);
		this.NaquadahSupplyOrb1.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.NaquadahSupplyOrb1_1 = new ModelRenderer(this, 0, 0);
		this.NaquadahSupplyOrb1_1.setRotationPoint(-4.1F, 12.0F, -20.0F);
		this.NaquadahSupplyOrb1_1.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);

		this.Lid = new ModelRenderer(this, 0, 0);
		this.Lid.setRotationPoint(-8.0F, 7.9F, -8.0F);
		this.Lid.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);

		for(int i = 0; i < LAYERS.length; i++){
			ModelRenderer layer = new ModelRenderer(this, 0, 0);
			layer.setRotationPoint(-7.9f, 22.32F - (i * 1.58F), -7.9f);
			layer.addBox(0.0F, 0.0F, 0.0F, 10, 1, 10, 0.0F);
			LAYERS[i] = layer;
		}
		this.Base = new ModelRenderer(this, 0, 0);
		this.Base.setRotationPoint(-8.0F, 23.9F, -8.0F);
		this.Base.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {


		for(int i = 0; i < LAYERS.length; i++){
			ModelRenderer layer = LAYERS[i];
			if(layer == null)
				continue;
			ResourceLocation layerTexture = LAYER_LOCATIONS[i];
			if(layerTexture == null)
				layerTexture = new ResourceLocation(MineGate.MODID + ":textures/reactor/blank_reactor_component.png");
			Minecraft.getMinecraft().renderEngine.bindTexture(layerTexture);
			render(layer, f5, 1.58, 1.58, 1.58);
		}

		ResourceLocation textures = new ResourceLocation(MineGate.MODID + ":textures/reactor/casing.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);

		render(CaseNegZ, f5, 1.0, 1.0, 0.1);

//		this.NaquadahPowerSupply.render(f5);
//		this.NaquadahPowerSupply_1.render(f5);
//
//		this.NaquadahSupplyOrb1.render(f5);
//		this.NaquadahSupplyOrb1_1.render(f5);

		render(Lid, f5, 1.0, 0.1, 1.0);

		render(CasePosX, f5, 0.1, 1.0, 1.0);
		render(CasePosZ, f5, 1.0, 1.0, 0.1);

		render(CasePosX, f5, 0.1, 1.0, 1.0);
		render(Base, f5, 1.0, 0.1, 1.0);
	}

	public void render(ModelRenderer model, float f5, double scaleX, double scaleY, double scaleZ){
		GL11.glPushMatrix();
		GL11.glTranslatef(model.offsetX, model.offsetY, model.offsetZ);
		GL11.glTranslatef(model.rotationPointX * f5, model.rotationPointY * f5, model.rotationPointZ * f5);
		GL11.glScaled(scaleX, scaleY, scaleZ);
		GL11.glTranslatef(-model.offsetX, -model.offsetY, -model.offsetZ);
		GL11.glTranslatef(-model.rotationPointX * f5, -model.rotationPointY * f5, -model.rotationPointZ * f5);
		model.render(f5);
		GL11.glPopMatrix();
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
