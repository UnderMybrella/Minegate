package org.abimon.mods.minecraft.minegate;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class NaquadahOreGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;
		//System.out.println("All the cool kids do world gen");
		if(world.provider.dimensionId == 0)
		{
			//for(int k = 0; k < 1; k++) {
			int chance = 2;

			if(MineGate.doesClassExist("moze_intel.projecte.api.ProjectEAPI"))
				chance = 16;

			if(random.nextInt(chance) == 0)
			{
				int firstBlockXCoord = chunkX + random.nextInt(16);
				int firstBlockYCoord = random.nextInt(10);
				int firstBlockZCoord = chunkZ + random.nextInt(16);
				new WorldGenMinable(MineGate.naquadahOre, 3).generate(world, random, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
				//System.out.println("Doing stuff at " + firstBlockXCoord + ", " + firstBlockYCoord + ", " + firstBlockZCoord);
				//}
			}
		}
	}

}
