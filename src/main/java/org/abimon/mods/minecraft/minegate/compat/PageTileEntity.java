package org.abimon.mods.minecraft.minegate.compat;

import java.lang.reflect.Constructor;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.lexicon.page.PageText;

public class PageTileEntity extends LexiconPage {

	TileEntity te;
	int relativeMouseX, relativeMouseY;
	boolean tooltipEntity;
	int size;
	int width;
	int height;
	Constructor entityConstructor;

	long existed = 0;

	public PageTileEntity(String unlocalizedName, TileEntity te, int width, int height, int size) {
		super(unlocalizedName);
		this.te = te;
		this.width = width;
		this.height = height;
		this.size = size;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		int text_x = gui.getLeft() + 16;
		int text_y = gui.getTop() + gui.getHeight() - 40;
		int entity_scale = getEntityScale(size);
		int entity_x = gui.getLeft() + gui.getWidth() / 2;
		int entity_y = gui.getTop() + gui.getHeight() / 2 + MathHelper.floor_float(height * entity_scale / 2);

		renderEntity(gui, te, entity_x, entity_y, entity_scale, existed * 2);

		PageText.renderText(text_x, text_y, gui.getWidth() - 30, gui.getHeight(), getUnlocalizedName());
	}

	@SideOnly(Side.CLIENT)
	public int getEntityScale(int targetSize) {
		float entity_size = width;

		if(width < height)
			entity_size = height;

		return MathHelper.floor_float(size / entity_size);

	}

	@Override
	public void updateScreen() {
		existed++;
	}

	@SideOnly(Side.CLIENT)
	public void renderEntity(IGuiLexiconEntry gui, TileEntity entity, int x, int y, int scale, float rotation) {
		entity.setWorldObj(Minecraft.getMinecraft() != null ? Minecraft.getMinecraft().theWorld : null);

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 75.0F);
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180F, 0.0F, 0.25F, 1.0F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glTranslatef(0.0F, 0, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		TileEntitySpecialRenderer render = TileEntityRendererDispatcher.instance.getSpecialRenderer(te);
		if(render != null)
			render.renderTileEntityAt(entity, 0, 0, 0, 1);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		if(relativeMouseX >= x - width * scale / 2 - 10  && relativeMouseY >= y - height * scale - 20 && relativeMouseX <= x + width * scale / 2 + 10 && relativeMouseY <= y + 20)
			tooltipEntity = true;
	}
}
