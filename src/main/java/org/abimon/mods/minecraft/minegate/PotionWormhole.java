package org.abimon.mods.minecraft.minegate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class PotionWormhole extends Potion {

	protected PotionWormhole(int p_i1573_1_, boolean p_i1573_2_, int p_i1573_3_) {
		super(p_i1573_1_, p_i1573_2_, p_i1573_3_);
	}
	
    protected Potion setIconIndex(int p_76399_1_, int p_76399_2_)
    {
       return super.setIconIndex(p_76399_1_, p_76399_2_);
    }
    
    public void performEffect(EntityLivingBase base, int p_76394_2_){
    }
    
    public boolean isReady(int p_76397_1_, int p_76397_2_)
    {
    	return true;
    }

}
