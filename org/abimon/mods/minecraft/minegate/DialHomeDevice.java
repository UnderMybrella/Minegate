package org.abimon.mods.minecraft.minegate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Dial Home Device - Undermybrella
 * Created using Tabula 4.1.1
 */
public class DialHomeDevice extends ModelBase {
	public ModelRenderer MainBody;
	public ModelRenderer Button;
	public ModelRenderer GlyphButtonFront_1;
	public ModelRenderer GlyphButtonLeft_2;
	public ModelRenderer GlyphButtonRight_1;
	public ModelRenderer GlyphButtonBack_2;
	public ModelRenderer GlyphButtonCorner_2;
	public ModelRenderer GlyphButtonCorner_1;
	public ModelRenderer GlyphButtonCorner_3;
	public ModelRenderer GlyphButtonCorner_4;
	public ModelRenderer GlyphButtonFront_2;
	public ModelRenderer GlyphButtonRight_2;
	public ModelRenderer GlyphButtonBack_1;
	public ModelRenderer GlyphButtonLeft_1;

	public ModelRenderer GlyphButtonDownA;
	public ModelRenderer GlyphButtonDownB;
	public ModelRenderer GlyphButtonDownC;
	public ModelRenderer GlyphButtonDownD;
	public ModelRenderer GlyphButtonDownE;
	public ModelRenderer GlyphButtonDownF;
	public ModelRenderer GlyphButtonDownG;
	public ModelRenderer GlyphButtonDownH;
	public ModelRenderer GlyphButtonDownT;

	public TileEntityDHD te;

	public DialHomeDevice(boolean baseModel) {
		if(baseModel)
			this.textureWidth = 16;
		else
			this.textureWidth = 4;
		this.textureHeight = textureWidth;

		this.MainBody = new ModelRenderer(this, 0, 0);
		this.MainBody.setRotationPoint(-8.0F, 8.0F, -8.0F);
		this.MainBody.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);

		this.GlyphButtonRight_1 = new ModelRenderer(this, 16, 44);
		this.GlyphButtonRight_1.setRotationPoint(4.0F, 6.0F, -4.0F);
		this.GlyphButtonRight_1.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonRight_2 = new ModelRenderer(this, 16, 44);
		this.GlyphButtonRight_2.setRotationPoint(4.0F, 6.0F, 0.0F);
		this.GlyphButtonRight_2.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);


		this.GlyphButtonBack_1 = new ModelRenderer(this, 0, 44);
		this.GlyphButtonBack_1.setRotationPoint(0.0F, 6.0F, 4.0F);
		this.GlyphButtonBack_1.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonBack_2 = new ModelRenderer(this, 0, 44);
		this.GlyphButtonBack_2.setRotationPoint(-4.0F, 6.0F, 4.0F);
		this.GlyphButtonBack_2.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);



		this.GlyphButtonLeft_1 = new ModelRenderer(this, 16, 44);
		this.GlyphButtonLeft_1.setRotationPoint(-8.0F, 6.0F, 0.0F);
		this.GlyphButtonLeft_1.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonLeft_2 = new ModelRenderer(this, 16, 44);
		this.GlyphButtonLeft_2.setRotationPoint(-8.0F, 6.0F, -4.0F);
		this.GlyphButtonLeft_2.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);



		this.GlyphButtonCorner_1 = new ModelRenderer(this, 32, 45);
		this.GlyphButtonCorner_1.setRotationPoint(-8.0F, 7.0F, 4.0F);
		this.GlyphButtonCorner_1.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);

		this.GlyphButtonCorner_2 = new ModelRenderer(this, 32, 45);
		this.GlyphButtonCorner_2.setRotationPoint(4.0F, 7.0F, 4.0F);
		this.GlyphButtonCorner_2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);

		this.GlyphButtonCorner_3 = new ModelRenderer(this, 32, 45);
		this.GlyphButtonCorner_3.setRotationPoint(4.0F, 7.0F, -8.0F);
		this.GlyphButtonCorner_3.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);

		this.GlyphButtonCorner_4 = new ModelRenderer(this, 32, 45);
		this.GlyphButtonCorner_4.setRotationPoint(-8.0F, 7.0F, -8.0F);
		this.GlyphButtonCorner_4.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4, 0.0F);



		this.GlyphButtonFront_1 = new ModelRenderer(this, 0, 44);
		this.GlyphButtonFront_1.setRotationPoint(-4.0F, 6.0F, -8.0F);
		this.GlyphButtonFront_1.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonFront_2 = new ModelRenderer(this, 0, 44);
		this.GlyphButtonFront_2.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.GlyphButtonFront_2.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.Button = new ModelRenderer(this, 0, 32);
		this.Button.setRotationPoint(-4.0F, 4.0F, -4.0F);
		this.Button.addBox(0.0F, 0.0F, 0.0F, 8, 4, 8, 0.0F);

		this.GlyphButtonDownA = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownA.setRotationPoint(8.0F, 8.0F, 0.0F);
		this.GlyphButtonDownA.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownB = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownB.setRotationPoint(8.0F, 8.0F, -4.0F);
		this.GlyphButtonDownB.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownC = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownC.setRotationPoint(8.0F, 8.0F, -8.0F);
		this.GlyphButtonDownC.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownD = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownD.setRotationPoint(8.0F, 8.0F, -12.0F);
		this.GlyphButtonDownD.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownE = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownE.setRotationPoint(4.0F, 8.0F, -12.0F);
		this.GlyphButtonDownE.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownF = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownF.setRotationPoint(0.0F, 8.0F, -12.0F);
		this.GlyphButtonDownF.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownG = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownG.setRotationPoint(-4.0F, 8.0F, -12.0F);
		this.GlyphButtonDownG.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownH = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownH.setRotationPoint(-8.0F, 8.0F, -12.0F);
		this.GlyphButtonDownH.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);

		this.GlyphButtonDownT = new ModelRenderer(this, 8, 35);
		this.GlyphButtonDownT.setRotationPoint(8.0F, 8.0F, 4.0F);
		this.GlyphButtonDownT.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if(textureWidth == 16){
			ResourceLocation textures = new ResourceLocation("minecraft:textures/blocks/stone.png");
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);

			this.MainBody.render(f5);

			textures = new ResourceLocation("minecraft:textures/blocks/redstone_block.png");
			Minecraft.getMinecraft().renderEngine.bindTexture(textures);

			this.Button.render(f5);
		}
		else{

			final String glyph = ":textures/blocks/glyphs/hydra.png";

			renderObject(GlyphButtonRight_1, te.glyphs[0], f5, te.pressed[0]);
			renderObject(GlyphButtonRight_2, te.glyphs[1], f5, te.pressed[1]);
			renderObject(GlyphButtonLeft_1, te.glyphs[2], f5, te.pressed[2]);
			renderObject(GlyphButtonLeft_2, te.glyphs[3], f5, te.pressed[3]);

			renderObject(GlyphButtonFront_1, te.glyphs[4], f5, te.pressed[4]);
			renderObject(GlyphButtonFront_2, te.glyphs[5], f5, te.pressed[5]);
			renderObject(GlyphButtonBack_1, te.glyphs[6], f5, te.pressed[6]);
			renderObject(GlyphButtonBack_2, te.glyphs[7], f5, te.pressed[7]);

			renderObject(GlyphButtonCorner_1, te.glyphs[8], f5, te.pressed[8]);
			renderObject(GlyphButtonCorner_2, te.glyphs[9], f5, te.pressed[9]);
			renderObject(GlyphButtonCorner_3, te.glyphs[10], f5, te.pressed[10]);
			renderObject(GlyphButtonCorner_4, te.glyphs[11], f5, te.pressed[11]);

			renderObject(GlyphButtonDownA, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownB, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownC, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownD, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownE, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownF, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownG, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownH, MineGate.MODID + glyph, f5);
			renderObject(GlyphButtonDownT, MineGate.MODID + glyph, f5);
		}
	}

	public void renderObject(ModelRenderer renderer, Glyph glyph, float f5){
		renderObject(renderer, glyph.resourceLocation, f5);
	}

	public void renderObject(ModelRenderer renderer, Glyph glyph, float f5, boolean pressed){
		if(glyph == null)
			renderObject(renderer, "", f5, pressed);
		else
			renderObject(renderer, glyph.resourceLocation, f5, pressed);
	}

	public void renderObject(ModelRenderer renderer, String glyphAddress, float f5){
		renderObject(renderer, glyphAddress, f5, false);
	}

	public void renderObject(ModelRenderer renderer, String glyphAddress, float f5, boolean pressed){
		if(!glyphAddress.endsWith(".png"))
			glyphAddress += ".png";
		ResourceLocation textures = new ResourceLocation(MineGate.MODID + ":textures/blocks/dhd.png");
		if(pressed)
			textures = new ResourceLocation(MineGate.MODID + ":textures/blocks/dhd_activated.png");
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		renderer.render(f5);
		textures = new ResourceLocation(glyphAddress);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		renderer.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setTileEntity(TileEntity te) {
		this.te = (TileEntityDHD) te;
	}
}
