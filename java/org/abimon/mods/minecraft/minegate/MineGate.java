package org.abimon.mods.minecraft.minegate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.abimon.mods.minecraft.minegate.blocks.BlockChevron;
import org.abimon.mods.minecraft.minegate.blocks.BlockDHD;
import org.abimon.mods.minecraft.minegate.blocks.BlockDHDGhost;
import org.abimon.mods.minecraft.minegate.blocks.BlockDisplay;
import org.abimon.mods.minecraft.minegate.blocks.BlockDisplayContainer;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquityProvider;
import org.abimon.mods.minecraft.minegate.blocks.BlockRedstoneControl;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargateController;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargateFrame;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargatePortal;
import org.abimon.mods.minecraft.minegate.blocks.BlockStorage;
import org.abimon.mods.minecraft.minegate.blocks.BlockWeakNaquadahOre;
import org.abimon.mods.minecraft.minegate.client.DHDRenderer;
import org.abimon.mods.minecraft.minegate.client.MineGateRenderer;
import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.compat.CoFHCompatibility;
import org.abimon.mods.minecraft.minegate.compat.ComputerCraftCompatibility;
import org.abimon.mods.minecraft.minegate.compat.IC2Compatibility;
import org.abimon.mods.minecraft.minegate.compat.ProjectECompatibility;
import org.abimon.mods.minecraft.minegate.compat.WAILACompatibility;
import org.abimon.mods.minecraft.minegate.items.ItemBlockMeta;
import org.abimon.mods.minecraft.minegate.items.ItemBlockMetaWithName;
import org.abimon.mods.minecraft.minegate.items.ItemNaquadahDerivatives;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityChevron;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityControl;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityFakePortal;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityGlyphs;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquityProvider;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargatePortal;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = MineGate.MODID, version = MineGate.VERSION, dependencies = "after:Botania;after:Thaumcraft")
public class MineGate
{
	public static final String MODID = "MineGate";
	public static final String VERSION = "1.0";

	public static final String[] GLYPHS = new String[]{"andromeda", "aquarius", "aquila", "aries", "auriga", "bootes", "cancer", "canis_minor", "capricornus", "centaurus", "cetus", "corona_australis", "crater", "earth", "equuleus", "eridanus", "gemini", "hydra", "leo_minor", "leo", "libra", "lynx", 
			"microscopium", "monoceros", "norma", "orion", "pegasus", "perseus", "pisces", "piscis_austrinus", "sagittarius", "scorpius", "sculptor", "scutum", "serpens_caput", "sextans", "taurus", "triangulum", "virgo"};

	public static final CreativeTabs creative = new CreativeTabs(MODID + ":creativeTab"){

		@Override
		public Item getTabIconItem() {
			return glyphs;
		}

		public int func_151243_f(){
			for(int i = 0; i < glyphList.size(); i++)
				if(glyphList.get(i).glyphName.equals("earth"))
					return i;
			return 0;
		}

	};

	public static final Block chevron = new BlockChevron().setBlockName(MODID + ":chevron").setBlockTextureName(MODID + ":chevron").setCreativeTab(creative).setHardness(9999f).setResistance(Float.MAX_VALUE);

	public static final Block stargateFrame = new BlockStargateFrame(Material.rock).setBlockName(MODID + ":frame").setBlockTextureName(MODID + ":frame").setCreativeTab(creative).setHardness(9999f).setResistance(Float.MAX_VALUE);
	public static final Block stargateController = new BlockStargateController().setBlockName(MODID + ":controller").setBlockTextureName(MODID + ":controller").setCreativeTab(creative).setHardness(9999f).setResistance(Float.MAX_VALUE);
	public static final Block glyphBlock = new BlockDisplayContainer(){
		@SideOnly(Side.CLIENT)
		@Override
		public void registerBlockIcons(IIconRegister register)	{
			for(int i = 0; i < glyphList.size(); i++)
				glyphList.get(i).blockIcon = register.registerIcon(glyphList.get(i).resourceLocation);
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIcon(IBlockAccess access, int x, int y, int z, int p_149673_5_)
		{
			try{
				TileEntityGlyphs glyphTE = (TileEntityGlyphs) access.getTileEntity(x, y, z);
				for(Glyph glyph : glyphList)
					if(glyph.glyphName.equals(glyphTE.glyphName))
						return glyph.blockIcon;
			}
			catch(Throwable th){
				th.printStackTrace();
			}
			return Blocks.stone.getIcon(0, 0);
		}

		@Override
		public int getRenderType(){
			return renderID;
		}

		@Override
		public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
			return new TileEntityGlyphs();
		}

		public void onBlockClicked(World world, int x, int y, int z, EntityPlayer p_149699_5_) {
			try{
				TileEntityGlyphs glyphTE = (TileEntityGlyphs) world.getTileEntity(x, y, z);
				for(int i = 0; i < glyphList.size(); i++)
					if(glyphList.get(i).glyphName.equals(glyphTE.glyphName))
					{
						EntityItem item = new EntityItem(world, x, y, z, new ItemStack(glyphs, 1, i));
						world.spawnEntityInWorld(item);
						world.setBlock(x, y, z, stargateFrame);
						world.removeTileEntity(x, y, z);
					}
			}
			catch(Throwable th){}
		}

		public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
		{
			try{
				TileEntityGlyphs glyphTE = (TileEntityGlyphs) world.getTileEntity(x, y, z);
				if(!glyphTE.glyphName.equals(glyphList.get(player.getEquipmentInSlot(0).getItemDamage()).glyphName))
					for(int i = 0; i < glyphList.size(); i++)
						if(glyphList.get(i).glyphName.equals(glyphTE.glyphName))
						{
							if(!world.isRemote && !player.capabilities.isCreativeMode){
								EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(glyphs, 1, i));
								world.spawnEntityInWorld(item);
							}
							glyphTE.glyphName = glyphList.get(player.getEquipmentInSlot(0).getItemDamage()).glyphName;
							world.markBlockForUpdate(x, y, z);
							if(!player.capabilities.isCreativeMode)
								player.getEquipmentInSlot(0).stackSize--;
							break;
						}
			}
			catch(Throwable th){}
			return false;
		}
	};

	public static final Block stargatePortal = new BlockStargatePortal().setBlockTextureName("portal").setBlockName(MODID + ":portal").setHardness(Float.MAX_VALUE).setResistance(Float.MAX_VALUE);

	public static final DamageSource stargateDamage = new DamageSource(MODID + ":stargateDamage").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();

	public static final Item glyphs = new Item(){
		@SideOnly(Side.CLIENT)
		public void registerIcons(IIconRegister register){
			for(int i = 0; i < glyphList.size(); i++)
				glyphList.get(i).itemIcon = register.registerIcon(glyphList.get(i).resourceLocation);
		}

		@SideOnly(Side.CLIENT)
		public void getSubItems(Item item, CreativeTabs p_150895_2_, List list)
		{
			for(int i = 0; i < glyphList.size(); i++)
				list.add(new ItemStack(item, 1, i));
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIconFromDamage(int damage)
		{
			return glyphList.get(Math.min(damage, glyphList.size())).itemIcon;
		}

		public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
			list.add(StatCollector.translateToLocal(MODID + ":" + GLYPHS[item.getItemDamage()]));
		}

	}.setCreativeTab(creative).setHasSubtypes(true).setUnlocalizedName(MODID + ":glyphs");

	public static final Item glyphSetter = new Item(){

		public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {


			if(!item.hasTagCompound())
				item.setTagCompound(new NBTTagCompound());

			if(item.getTagCompound().hasKey("Glyphs")){
				list.add("Glyphs: ");
				for(int i : item.getTagCompound().getIntArray("Glyphs"))
					list.add(GLYPHS[i]);
			}
		}

	}.setCreativeTab(creative).setTextureName(MODID + ":glyphSetter").setUnlocalizedName(MODID + ":glyphSetter");

	public static final Item dhdBinder = new Item(){
		@SideOnly(Side.CLIENT)
		public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
		{
			return Color.blue.getRGB();
		}

		public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
			if(!item.hasTagCompound())
				item.setTagCompound(new NBTTagCompound());

			list.add("BoundX: " + item.getTagCompound().getInteger("BoundX"));
			list.add("BoundY: " + item.getTagCompound().getInteger("BoundY"));
			list.add("BoundZ: " + item.getTagCompound().getInteger("BoundZ"));
		}
	}.setCreativeTab(creative).setTextureName(MODID + ":glyphSetter").setUnlocalizedName(MODID + ":dhdBinder");

	public static final Block redstoneControl = new BlockRedstoneControl(Material.rock).setBlockName(MODID + ":redstoneControl").setBlockTextureName(MODID + ":redstoneControl").setCreativeTab(creative);
	public static final Block dialHomeDevice = new BlockDHD(Material.rock).setBlockTextureName(MODID + ":dhd_icon").setCreativeTab(creative).setBlockName(MODID + ":dhd").setHardness(9999f).setResistance(99f);
	public static final Block dhdGhost = new BlockDHDGhost(Material.rock).setBlockTextureName(MODID + ":dhd_icon").setBlockName(MODID + ":dhd").setHardness(99f).setResistance(99f);

	public static final Block storage = new BlockStorage().setCreativeTab(creative).setBlockTextureName(MODID + ":storage").setBlockName(MODID + ":storage").setHardness(36f).setResistance(999f);

	public static final Block naquadahOre = new BlockWeakNaquadahOre().setCreativeTab(creative).setBlockTextureName(MODID + ":weak_naquadah_ore").setBlockName(MODID + ":weakNaquadahOre").setHardness(36f);
	public static final BlockNaquityProvider naquityProvider = (BlockNaquityProvider) new BlockNaquityProvider(Material.rock).registerNewType(0, TileEntityNaquityProvider.class).setCreativeTab(creative).setBlockName(MODID + ":naquityProvider").setBlockTextureName(MODID + ":naquityProvider").setHardness(9999f).setResistance(Float.MAX_VALUE);

	public static final Item naquadahDerivatives = new ItemNaquadahDerivatives().setHasSubtypes(true).setCreativeTab(creative);

	public static PotionWormhole wormholeEffect;

	public static HashMap<Glyph[], Location> addressToController = new HashMap<Glyph[], Location>();

	private static LinkedList<TileEntityStargateController> registering = new LinkedList<TileEntityStargateController>();

	public static LinkedList<Glyph> glyphList = new LinkedList<Glyph>();

	public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event){
		for(String s : GLYPHS)
			glyphList.add(new Glyph(s, MODID + ":glyphs/" + s));

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	@SideOnly(Side.CLIENT)
	public void init(FMLInitializationEvent event){
		RenderingRegistry.registerBlockHandler(renderID, new MineGateRenderer()); 
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDHD.class, new DHDRenderer());
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{

		for(int i = 1; i < Potion.potionTypes.length; i++)
			if(Potion.potionTypes[i] == null){
				wormholeEffect = new PotionWormhole(i, false, new Color(15, 63, 255).getRGB());
				wormholeEffect.setPotionName(MODID + ":wormholeEffect");
				wormholeEffect.setIconIndex(0, 1);
				break;
			}

		while(true){
			boolean changed = false;
			for(int i = 0; i < glyphList.size() - 1; i++)
				if(!oneComesBefore(glyphList.get(i).glyphName, glyphList.get(i+1).glyphName))
				{
					Glyph tmp = glyphList.get(i);
					glyphList.set(i, glyphList.get(i+1));
					glyphList.set(i+1, tmp);
					changed = true;
				}
			if(!changed)
				break;
		}

		GameRegistry.registerBlock(stargateFrame, "frame");
		GameRegistry.registerBlock(stargateController, "controller");
		GameRegistry.registerBlock(stargatePortal, "portal");
		GameRegistry.registerBlock(chevron, "chevron");
		GameRegistry.registerBlock(dialHomeDevice, "dhd");
		GameRegistry.registerBlock(dhdGhost, "dhdGhost");

		GameRegistry.registerBlock(glyphBlock, "glyphBlock");
		GameRegistry.registerItem(glyphs, "glyphs");

		GameRegistry.registerItem(glyphSetter, "glyphSetter");
		GameRegistry.registerItem(dhdBinder, "dhdBinder");

		GameRegistry.registerBlock(naquityProvider, ItemBlockMeta.class, "naquityProvider");

		GameRegistry.registerBlock(naquadahOre, "weakNaquadahOre");
		GameRegistry.registerItem(naquadahDerivatives, "naquadahDerivatives");
		GameRegistry.registerBlock(storage, ItemBlockMetaWithName.class, "storage");

		GameRegistry.registerBlock(redstoneControl, "redstoneControl");
		
		GameRegistry.registerTileEntity(TileEntityStargateController.class, "controller");
		GameRegistry.registerTileEntity(TileEntityGlyphs.class, "glyphs");
		GameRegistry.registerTileEntity(TileEntityStargatePortal.class, "portal");
		GameRegistry.registerTileEntity(TileEntityFakePortal.class, "fakePortal");
		GameRegistry.registerTileEntity(TileEntityChevron.class, "chevron");
		GameRegistry.registerTileEntity(TileEntityDHD.class, "dhd");
		GameRegistry.registerTileEntity(TileEntityNaquityProvider.class, "naquityProvider");
		GameRegistry.registerTileEntity(TileEntityControl.class, "control");

		GameRegistry.addRecipe(new ItemStack(naquadahDerivatives, 1, 1), new Object[]{"NNN", "NIN", "NNN", 'N', new ItemStack(naquadahDerivatives, 1, 1), 'I', Blocks.iron_block});
		GameRegistry.addRecipe(new ItemStack(naquadahDerivatives, 1, 1), new Object[]{"IGI", "GNG", "IGI", 'I', Blocks.iron_block, 'G', Blocks.gold_block, 'N', new ItemStack(naquadahDerivatives, 1, 2)});
		GameRegistry.addRecipe(new ItemStack(naquadahDerivatives, 1, 2), new Object[]{"III", "INI", "III", 'N', new ItemStack(naquadahDerivatives, 1, 1), 'I', Blocks.iron_block});

		GameRegistry.addShapelessRecipe(new ItemStack(glyphs), new Object[]{Items.paper, Items.paper, Items.paper, Items.redstone, Items.dye});

		GameRegistry.addRecipe(new ItemStack(stargateFrame), new Object[]{"SSS", "SNS", "SSS", 'S', Blocks.stone, 'N', new ItemStack(naquadahDerivatives, 1, 0)});
		GameRegistry.addRecipe(new ItemStack(stargateController), new Object[]{"SRS", "RFR", "SRS", 'S', Blocks.stone, 'R', Items.redstone, 'F', stargateFrame});
		GameRegistry.addRecipe(new ItemStack(chevron), new Object[]{"SGS", "SFS", "SSS", 'S', Blocks.stone, 'G', new ItemStack(glyphs, 1, OreDictionary.WILDCARD_VALUE), 'F', stargateFrame});
		GameRegistry.addRecipe(new ItemStack(dialHomeDevice), new Object[]{"GGG", "GFG", "GGG", 'G', new ItemStack(glyphs, 1, OreDictionary.WILDCARD_VALUE), 'F', stargateFrame});

		GameRegistry.addShapelessRecipe(new ItemStack(glyphSetter), new Object[]{Items.paper, Items.paper, new ItemStack(glyphs, 1, OreDictionary.WILDCARD_VALUE)});
		GameRegistry.addShapelessRecipe(new ItemStack(dhdBinder), new Object[]{Items.paper, Items.paper, new ItemStack(glyphs, 1, OreDictionary.WILDCARD_VALUE), Items.redstone});

		for(int i = 0; i < 4; i++)
			GameRegistry.addRecipe(new ItemStack(storage, 1, i), new Object[]{"NNN", "NNN", "NNN", 'N', new ItemStack(naquadahDerivatives, 1, i)});

		GameRegistry.addRecipe(new ItemStack(naquityProvider), new Object[]{"SIS", "IFI", "SIS", 'S', Blocks.stone, 'I', Blocks.iron_block, 'F', stargateFrame});

		GameRegistry.addSmelting(new ItemStack(naquadahDerivatives, 1, 1), new ItemStack(naquadahDerivatives, 1, 0), 1.0f);

		for(int i = 0; i < glyphList.size() - 1; i++)
			GameRegistry.addShapelessRecipe(new ItemStack(glyphs, 1, i + 1), new Object[]{new ItemStack(glyphs, 1, i)});
		GameRegistry.addShapelessRecipe(new ItemStack(glyphs, 1, 0), new Object[]{new ItemStack(glyphs, 1, glyphList.size() - 1)});

		CraftingManager.getInstance().getRecipeList().add(new GlyphSetter());

		if(doesClassExist("cofh.api.energy.IEnergyReceiver"))
			new CoFHCompatibility();
		if(doesClassExist("ic2.api.energy.tile.IEnergySink"))
			new IC2Compatibility();
		if(doesClassExist("dan200.computercraft.api.ComputerCraftAPI"))
			new ComputerCraftCompatibility();
		if(doesClassExist("moze_intel.projecte.api.ProjectEAPI"))
			new ProjectECompatibility();
		if(doesClassExist("mcp.mobius.waila.api.IWailaRegistrar"))
			new WAILACompatibility();
		if(doesClassExist("vazkii.botania.api.BotaniaAPI"))
			new BotaniaCompatibility();

		GameRegistry.registerWorldGenerator(new NaquadahOreGenerator(), 1);
	}

	public static boolean doesClassExist(String clazz){
		try{
			Class.forName(clazz);
			return true;
		}
		catch(Throwable th){}
		return false;
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event){
		reloadNetwork();
	}

	public static void reloadNetwork(){

		try{
			System.out.println("Reloading Network...");

			addressToController.clear();
			for(TileEntityStargateController cont : registering)
			{
				if(cont.getWorldObj().isRemote)
				{
					registering.remove();
					continue;
				}
				addressToController.put(cont.address, new Location(cont));
				System.out.println(new Location(cont));
			}
		}
		catch(Throwable th){}
	}

	public static void addToRegister(TileEntityStargateController controller){
		System.out.println(registering);
		for(TileEntityStargateController cont : registering)
			if(new Location(cont).equals(new Location(controller)))
				return;
		registering.add(controller);
	}

	public static boolean oneComesBefore(String first, String second){
		if(first.equals(second))
			return true;
		for(int i = 0; i < (Math.min(first.length(), second.length())); i++)
			if(first.charAt(i) != second.charAt(i))
				return first.charAt(i) < second.charAt(i);
		if(first.length() < second.length())
			return true;
		return false;
	}

	private static LinkedList<PortalOutThread> portalOutPool = new LinkedList<PortalOutThread>();

	public static Thread getThreadFromPool(ThreadType type){
		if(type == ThreadType.MAKING_PORTAL_OUT)
		{
			if(portalOutPool.size() == 0)
				return new PortalOutThread();
			else
			{
				Thread thread = portalOutPool.removeFirst();
				System.out.println("Thread is in state: " + thread.getState());
				thread.stop();
				System.out.println("Thread is now in state: " + thread.getState());
				return thread;
			}
		}
		return null;
	}

	public static void returnThreadToPool(Thread thread){
		if(thread instanceof PortalOutThread)
			portalOutPool.add((PortalOutThread) thread);
	}

	public static void vaporiseEntity(Entity entity){
		if(entity.hurtResistantTime == 0)
		{
			entity.setPosition(entity.posX, -128, entity.posZ);
			entity.attackEntityFrom(stargateDamage, Float.MAX_VALUE);
		}
	}

}
