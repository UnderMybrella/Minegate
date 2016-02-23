package org.abimon.mods.minecraft.minegate.client;

import java.lang.reflect.Field;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;

public class ModelTopBox extends ModelBox {
	TexturedQuad[] quads = null;

	public ModelTopBox(ModelRenderer p_i1171_1_, int p_i1171_2_, int p_i1171_3_, float p_i1171_4_, float p_i1171_5_,
			float p_i1171_6_, float p_i1171_7_, float p_i1171_8_, float p_i1171_9_, float p_i1171_10_) {
		super(p_i1171_1_, p_i1171_2_, p_i1171_3_, p_i1171_4_, p_i1171_5_, p_i1171_6_, (int) p_i1171_7_, (int) p_i1171_8_, (int) p_i1171_9_,
				p_i1171_10_);

		try{
			Field field = ModelBox.class.getDeclaredField("quadList");
			quads = (TexturedQuad[]) field.get(this);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}

	/** To render only the top:
	 *  TexturedQuad[] quads = (TexturedQuad[]) field.get(this);
		field.set(this, new TexturedQuad[]{quads[2]});
	 */

	@SideOnly(Side.CLIENT)
	public void render(Tessellator tess, float someFloat)
	{
		quads[2].draw(tess, someFloat);
	}

}
