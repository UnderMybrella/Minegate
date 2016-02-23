package org.abimon.mods.minecraft.minegate.blocks;

import java.util.List;

import org.abimon.mods.minecraft.minegate.INaquadahBlock;
import org.abimon.mods.minecraft.minegate.naquity.EnumEnergy;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquityProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNaquityProvider extends BlockContainer implements INaquadahBlock{

	private Class<? extends TileEntity>[] classes = new Class[16];
	private IIcon[] icons = new IIcon[16];
	private String[] textureNames = new String[16];

	public BlockNaquityProvider(Material p_i45386_1_) {
		super(p_i45386_1_);

	}
	
    public int getMobilityFlag()
    {
    	return 2;
    }

	public BlockNaquityProvider registerNewType(int meta, Class<? extends TileEntity> clazz)
	{
		if(clazz != null)
			classes[meta] = clazz;
		return this;
	}

	public BlockNaquityProvider registerNewType(int meta, Class<? extends TileEntity> clazz, String textureName)
	{
		if(clazz != null){
			classes[meta] = clazz;
			textureNames[meta] = textureName;
		}
		return this;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(classes[meta] != null){
			try{
				return classes[meta].newInstance();
			}
			catch(Throwable th){}
		}
		return new TileEntityNaquityProvider();
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer p_149699_5_) {
		if(world.isRemote)
			return;
		if(world.getBlockMetadata(x, y, z) == 0)
		{
			TileEntityNaquityProvider prov = (TileEntityNaquityProvider) world.getTileEntity(x, y, z);
			System.out.println(prov.naquity);
			prov.naquity += 1.0f / EnumEnergy.HAND.getConversionRate();
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if(icons[meta] != null)
			return icons[meta];
		return this.blockIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		for(int i = 0; i < textureNames.length; i++)
			if(textureNames[i] != null && !textureNames[i].equals(""))
				icons[i] = register.registerIcon(textureNames[i]);
			else if(classes[i] != null)
				icons[i] = register.registerIcon(getTextureName());
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
	{
		for(int i = 0; i < classes.length; i++)
			if(classes[i] != null)
				p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	public int damageDropped(int damage)
	{
		return damage;
	}


}
