package org.abimon.mods.minecraft.minegate;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.compat.CoFHCompatibility;
import org.abimon.mods.minecraft.minegate.compat.IC2Compatibility;
import org.abimon.mods.minecraft.minegate.compat.MystcraftCompatibility;
import org.abimon.mods.minecraft.minegate.compat.ThaumcraftCompatibility;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class StargateGenerator implements IWorldGenerator {

	public static List<BiomeGenBase> BIOMES = new LinkedList<BiomeGenBase>();

	List<Integer> providers = new LinkedList<Integer>();

	Logger log;

	public StargateGenerator(){

		providers.add(0);

		log = LogManager.getLogger("[Minegate: Abydos]");

		if(BotaniaCompatibility.loaded)
			providers.add(8);
		if(CoFHCompatibility.loaded)
			providers.add(9);
		if(IC2Compatibility.loaded)
			providers.add(10);
		if(ThaumcraftCompatibility.loaded){
			providers.add(11);
			providers.add(12);
		}
	}

	static{
		BIOMES.add(BiomeGenBase.desert);
		BIOMES.add(BiomeGenBase.extremeHills);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {

		chunkX *= 16;
		chunkZ *= 16;

		if(world.provider.dimensionId == 0){ //Only gen in the overworld
			int chance = 128;

//			if(MystcraftCompatibility.loaded)
//				chance *= 4;

			BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
			if(BIOMES.contains(biome))
				if(new Random().nextInt(chance) == 0){
					int x = chunkX + random.nextInt(16);
					int y;
					int z = chunkZ + random.nextInt(16);

					for(y = 255; y > 0; y--){
						if(world.getBlock(chunkX, y, chunkZ) == biome.topBlock)
							break;
					}
					if(y == 0)
						for(y = 255; y > 0; y--){
							if(world.getBlock(chunkX, y, chunkZ) == Blocks.stone)
								break;
						}
					if(y == 0)
						y = 65; //Basic Default really

					if(y < 10)
						y = 5;
					else
						y -= 10;

					Glyph[] address = new Glyph[7];
					for(int i = 0; i < address.length; i++)
						address[i] = MineGate.glyphList.get(random.nextInt(MineGate.glyphList.size()));

					while(MineGate.addressToController.containsKey(address))
						for(int i = 0; i < address.length; i++)
							address[i] = MineGate.glyphList.get(random.nextInt(MineGate.glyphList.size()));

					world.setBlock(x, y, z, MineGate.stargateController);

					TileEntityStargateController controller = new TileEntityStargateController();
					controller.setWorldObj(world);
					controller.xCoord = x;
					controller.yCoord = y;
					controller.zCoord = z;
					controller.address = address;
					controller.direction = EnumDirection.X_POSITIVE; //So set the blocks along the z axis
					controller.naquityStored = random.nextFloat() * 10;

					int naquityProvider = providers.get(random.nextInt(providers.size()));

					world.setTileEntity(x, y, z, controller);

					world.setBlock(x, y, z-1, MineGate.stargateFrame);
					world.setBlock(x, y, z-2, MineGate.stargateFrame);

					world.setBlock(x, y+1, z-3, MineGate.stargateFrame);
					world.setBlock(x, y+2, z-4, MineGate.chevron);

					world.setBlock(x, y+3, z-5, MineGate.stargateFrame);
					world.setBlock(x, y+4, z-5, MineGate.stargateFrame);
					world.setBlock(x, y+5, z-5, MineGate.naquityProvider, naquityProvider, 3);
					world.setBlock(x, y+6, z-5, MineGate.stargateFrame);
					world.setBlock(x, y+7, z-5, MineGate.stargateFrame);

					world.setBlock(x, y+8, z-4, MineGate.chevron);
					world.setBlock(x, y+9, z-3, MineGate.chevron);

					world.setBlock(x, y+10, z-2, MineGate.stargateFrame);
					world.setBlock(x, y+10, z-1, MineGate.stargateFrame);


					world.setBlock(x, y+10, z, MineGate.chevron);


					world.setBlock(x, y, z+1, MineGate.stargateFrame);
					world.setBlock(x, y, z+2, MineGate.stargateFrame);

					world.setBlock(x, y+1, z+3, MineGate.stargateFrame);
					world.setBlock(x, y+2, z+4, MineGate.chevron);

					world.setBlock(x, y+3, z+5, MineGate.stargateFrame);
					world.setBlock(x, y+4, z+5, MineGate.stargateFrame);
					world.setBlock(x, y+5, z+5, MineGate.naquityProvider, naquityProvider, 3);
					world.setBlock(x, y+6, z+5, MineGate.stargateFrame);
					world.setBlock(x, y+7, z+5, MineGate.stargateFrame);

					world.setBlock(x, y+8, z+4, MineGate.chevron);
					world.setBlock(x, y+9, z+3, MineGate.chevron);

					world.setBlock(x, y+10, z+2, MineGate.stargateFrame);
					world.setBlock(x, y+10, z+1, MineGate.stargateFrame);

					log.info("Generating a Stargate at " + x + ", " + y + ", " + z);
				}
		}
	}

}
