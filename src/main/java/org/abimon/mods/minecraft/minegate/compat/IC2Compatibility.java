package org.abimon.mods.minecraft.minegate.compat;

import org.abimon.mods.minecraft.minegate.MineGate;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityEUProvider;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityManaNaquity;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityRFProvider;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.ExplosionIC2;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

public class IC2Compatibility {

	public static boolean loaded = false;
	
	public static IRecipe reactorIC2Recipe;

	public IC2Compatibility() {

		loaded = true;

		MineGate.naquityProvider.registerNewType(10, TileEntityEUProvider.class, MineGate.MODID + ":naquityProviderEU");
		GameRegistry.registerTileEntity(TileEntityEUProvider.class, "euNaquity");
		GameRegistry.addRecipe(new ItemStack(MineGate.naquityProvider, 1, 10), new Object[]{"SIS", "CFC", "SIS", 'S', Blocks.stone, 'I', Ic2Items.casingiron, 'C', Ic2Items.advancedCircuit, 'F', MineGate.stargateFrame});
		
		reactorIC2Recipe = MineGate.recipe(new ItemStack(MineGate.reactor), new Object[]{"S S", " R ", "S S", 'S', MineGate.stargateFrame, 'R', Ic2Items.nuclearReactor});
	}

	public static void doNuke(World world, int x, int y, int z, int strength) {
		ExplosionIC2 explosion = new ExplosionIC2(world, null, x, y, z, strength, 0.001F, ExplosionIC2.Type.Nuclear);
		explosion.doExplosion();

	}
}
