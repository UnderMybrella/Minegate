package org.abimon.mods.minecraft.minegate;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class Glyph 
{
	String glyphName = "";
	String resourceLocation = "";
	
	IIcon itemIcon;
	IIcon blockIcon;
	
	public Glyph(String glyphName, String resourceLocation){
		this.glyphName = glyphName;
		this.resourceLocation = resourceLocation;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(o instanceof Glyph)
			return ((Glyph) o).glyphName.equals(glyphName);
		return false;
	}
	
	public String toString(){
		return glyphName;
	}

}
