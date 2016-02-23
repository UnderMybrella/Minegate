package org.abimon.mods.minecraft.minegate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.abimon.mods.minecraft.minegate.blocks.BlockChevron;
import org.abimon.mods.minecraft.minegate.blocks.BlockDHD;
import org.abimon.mods.minecraft.minegate.blocks.BlockDHDGhost;
import org.abimon.mods.minecraft.minegate.blocks.BlockDisplay;
import org.abimon.mods.minecraft.minegate.blocks.BlockDisplayContainer;
import org.abimon.mods.minecraft.minegate.blocks.BlockHyperdrive;
import org.abimon.mods.minecraft.minegate.blocks.BlockMatterCannon;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquadahReactor;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquadahReactorFake;
import org.abimon.mods.minecraft.minegate.blocks.BlockNaquityProvider;
import org.abimon.mods.minecraft.minegate.blocks.BlockRedstoneControl;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargateController;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargateFrame;
import org.abimon.mods.minecraft.minegate.blocks.BlockStargatePortal;
import org.abimon.mods.minecraft.minegate.blocks.BlockStorage;
import org.abimon.mods.minecraft.minegate.blocks.BlockWeakNaquadahOre;
import org.abimon.mods.minecraft.minegate.client.CompressedMatterRenderer;
import org.abimon.mods.minecraft.minegate.client.DHDRenderer;
import org.abimon.mods.minecraft.minegate.client.MatterCannonRenderer;
import org.abimon.mods.minecraft.minegate.client.MineGateRenderer;
import org.abimon.mods.minecraft.minegate.client.NaquadahReactorRenderer;
import org.abimon.mods.minecraft.minegate.compat.BotaniaCompatibility;
import org.abimon.mods.minecraft.minegate.compat.CoFHCompatibility;
import org.abimon.mods.minecraft.minegate.compat.ComputerCraftCompatibility;
import org.abimon.mods.minecraft.minegate.compat.IC2Compatibility;
import org.abimon.mods.minecraft.minegate.compat.MystcraftCompatibility;
import org.abimon.mods.minecraft.minegate.compat.ProjectECompatibility;
import org.abimon.mods.minecraft.minegate.compat.ThaumcraftCompatibility;
import org.abimon.mods.minecraft.minegate.compat.WAILACompatibility;
import org.abimon.mods.minecraft.minegate.entity.EntityCompressedMatter;
import org.abimon.mods.minecraft.minegate.items.ItemBlockMeta;
import org.abimon.mods.minecraft.minegate.items.ItemBlockMetaWithName;
import org.abimon.mods.minecraft.minegate.items.ItemNaquadahDerivatives;
import org.abimon.mods.minecraft.minegate.items.ItemNaquadahFuelCell;
import org.abimon.mods.minecraft.minegate.items.ItemReactorComponent;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityChevron;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityControl;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityDHD;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityFakePortal;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityGlyphs;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityHyperDrive;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityMatterCannon;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquadahReactor;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityNaquityProvider;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargateController;
import org.abimon.mods.minecraft.minegate.tileentity.TileEntityStargatePortal;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
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

	public static final Item recovery = new Item(){
		public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
		{
			if(item.hasTagCompound() && item.getTagCompound().hasKey("Inventory") && item.getTagCompound().hasKey("TagType"))
				player.inventory.readFromNBT(item.getTagCompound().getTagList("Inventory", item.getTagCompound().getInteger("TagType")));
			item.stackSize = 0;
			return item;
		}

		@SideOnly(Side.CLIENT)
		public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
			list.add("For those times when the mod just bugs out... Sorry =/");
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIconFromDamage(int p_77617_1_)
		{
			return Items.apple.getIconFromDamage(p_77617_1_);
		}
	};

	public static final Block chevron = new BlockChevron().setBlockName(MODID + ":chevron").setBlockTextureName(MODID + ":chevron").setCreativeTab(creative).setHardness(9999f).setResistance(Float.MAX_VALUE);

	public static final Block stargateFrame = new BlockStargateFrame(Material.rock).setBlockName(MODID + ":frame").setBlockTextureName(MODID + ":frame").setCreativeTab(creative).setHardness(9999f).setResistance(Float.MAX_VALUE - 1);
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

	public static final Block hyperdrive = new BlockHyperdrive().setCreativeTab(creative).setBlockName(MODID + ":hyperdrive");

	public static final Block reactor = new BlockNaquadahReactor().setCreativeTab(creative).setBlockName(MODID + ":naquadahReactor").setBlockTextureName(MODID + ":reactor_icon");
	public static final Block reactorFake = new BlockNaquadahReactorFake().setBlockName(MODID + ":naquadahReactor");

	public static final Block matterCannon = new BlockMatterCannon().setCreativeTab(creative).setBlockName(MODID + ":naquadahMatterCannon").setBlockTextureName(MODID + ":matter_cannon");
	
	public static final Item naquadahDerivatives = new ItemNaquadahDerivatives().setHasSubtypes(true).setCreativeTab(creative);

	public static final Item naquadahFuelCell = new ItemNaquadahFuelCell().setCreativeTab(creative).setUnlocalizedName(MODID + ":naquadahFuelCell").setTextureName(MODID + ":naquadah_fuel_cell").setMaxDamage(Short.MAX_VALUE);
	public static final Item reactorComponents = new ItemReactorComponent().setCreativeTab(creative).setHasSubtypes(true);

	public static PotionWormhole wormholeEffect;

	public static HashMap<Glyph[], Location> addressToController = new HashMap<Glyph[], Location>();

	private static LinkedList<TileEntityStargateController> registering = new LinkedList<TileEntityStargateController>();
	private static ReentrantLock registerLock = new ReentrantLock();

	public static LinkedList<Glyph> glyphList = new LinkedList<Glyph>();

	public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

	public static SimpleNetworkWrapper network;

	public static Logger log = LogManager.getLogger("[Minegate]");

	public static IRecipe refinedNaquadahRecipe = recipe(new ItemStack(naquadahDerivatives, 1, 1), new Object[]{"NNN", "NIN", "NNN", 'N', new ItemStack(naquadahDerivatives, 1, 1), 'I', Blocks.iron_block});
	public static IRecipe refinedNaquadahRecipeCheap = recipe(new ItemStack(naquadahDerivatives, 1, 1), new Object[]{"IGI", "GNG", "IGI", 'I', Blocks.iron_block, 'G', Blocks.gold_block, 'N', new ItemStack(naquadahDerivatives, 1, 2)});
	public static IRecipe rawNaquadahRecipe = recipe(new ItemStack(naquadahDerivatives, 1, 2), new Object[]{"III", "INI", "III", 'N', new ItemStack(naquadahDerivatives, 1, 1), 'I', Blocks.iron_block});

	public static IRecipe naquadahReactor;

	public static boolean mystcraftLoaded = false;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event){
		for(String s : GLYPHS)
			glyphList.add(new Glyph(s, MODID + ":glyphs/" + s));

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	@SideOnly(Side.CLIENT)
	public void init(FMLInitializationEvent event){
		RenderingRegistry.registerBlockHandler(renderID, new MineGateRenderer()); 
		RenderingRegistry.registerEntityRenderingHandler(EntityCompressedMatter.class, new CompressedMatterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDHD.class, new DHDRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNaquadahReactor.class, new NaquadahReactorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterCannon.class, new MatterCannonRenderer());
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		EntityRegistry.registerModEntity(EntityCompressedMatter.class, "entitycompressedmatter", 0, this, 128, 100, true);

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

		GameRegistry.registerItem(recovery, "recovery");

		GameRegistry.registerBlock(redstoneControl, "redstoneControl");

		GameRegistry.registerBlock(reactor, "naquadahReactor");

		//GameRegistry.registerBlock(reactorFake, "naquadahReactorFake");

		GameRegistry.registerItem(naquadahFuelCell, "naquadahFuelCell");
		GameRegistry.registerItem(reactorComponents, "reactorComponents");
		
		GameRegistry.registerBlock(matterCannon, "matterCannon");

		//GameRegistry.registerBlock(hyperdrive, "hyperdrive"); Not currently working

		GameRegistry.registerTileEntity(TileEntityStargateController.class, "controller");
		GameRegistry.registerTileEntity(TileEntityGlyphs.class, "glyphs");
		GameRegistry.registerTileEntity(TileEntityStargatePortal.class, "portal");
		GameRegistry.registerTileEntity(TileEntityFakePortal.class, "fakePortal");
		GameRegistry.registerTileEntity(TileEntityChevron.class, "chevron");
		GameRegistry.registerTileEntity(TileEntityDHD.class, "dhd");
		GameRegistry.registerTileEntity(TileEntityNaquityProvider.class, "naquityProvider");
		GameRegistry.registerTileEntity(TileEntityControl.class, "control");
		GameRegistry.registerTileEntity(TileEntityHyperDrive.class, "hyperdrive");
		GameRegistry.registerTileEntity(TileEntityNaquadahReactor.class, "naquadahReactor");
		GameRegistry.registerTileEntity(TileEntityMatterCannon.class, "matterCannon");

		GameRegistry.addRecipe(refinedNaquadahRecipe);
		GameRegistry.addRecipe(refinedNaquadahRecipeCheap);
		GameRegistry.addRecipe(rawNaquadahRecipe);

		naquadahReactor = recipe(new ItemStack(reactor, 1, 0), new Object[]{"WPW", "WEW", "WPW", 'W', stargateFrame, 'P', naquityProvider, 'E', Items.emerald});
		GameRegistry.addRecipe(naquadahReactor);

		GameRegistry.addRecipe(new ItemStack(this.naquadahFuelCell), new Object[]{"INI", "INI", "INI", 'I', Items.iron_ingot, 'N', naquadahDerivatives});

		GameRegistry.addRecipe(new ItemStack(reactorComponents, 16), new Object[]{"ISI", "INI", "ISI", 'I', Items.iron_ingot, 'S', stargateFrame, 'N', naquadahDerivatives});
		GameRegistry.addRecipe(new ItemStack(reactorComponents, 1, 1), new Object[]{"APA", "SRS", "APA", 'A', Items.redstone, 'P', new ItemStack(Items.potionitem), 'R', reactorComponents, 'S', Items.sugar});
		for(int i = 2; i < 5; i++)
			GameRegistry.addRecipe(new ItemStack(reactorComponents, 1, i), new Object[]{"RRR", "RAR", "RRR", 'A', Items.redstone, 'R', new ItemStack(reactorComponents, 1, i-1)});

		GameRegistry.addShapelessRecipe(new ItemStack(glyphs), new Object[]{Items.paper, Items.paper, Items.paper, Items.redstone, Items.dye});

		GameRegistry.addRecipe(new ItemStack(stargateFrame, 4), new Object[]{"SSS", "SNS", "SSS", 'S', Blocks.stone, 'N', new ItemStack(naquadahDerivatives)});
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
		if(doesClassExist("com.xcompwiz.mystcraft.api.APIInstanceProvider"))
			new MystcraftCompatibility();
		if(doesClassExist("thaumcraft.api.ThaumcraftApi"))
			new ThaumcraftCompatibility();

		GameRegistry.registerWorldGenerator(new NaquadahOreGenerator(), 1);
		GameRegistry.registerWorldGenerator(new StargateGenerator(), 9999);
	}

	public static ShapedRecipes recipe(ItemStack p_92103_1_, Object ... p_92103_2_)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (p_92103_2_[i] instanceof String[])
		{
			String[] astring = (String[])((String[])p_92103_2_[i++]);

			for (int l = 0; l < astring.length; ++l)
			{
				String s1 = astring[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else
		{
			while (p_92103_2_[i] instanceof String)
			{
				String s2 = (String)p_92103_2_[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = new HashMap(); i < p_92103_2_.length; i += 2)
		{
			Character character = (Character)p_92103_2_[i];
			ItemStack itemstack1 = null;

			if (p_92103_2_[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item)p_92103_2_[i + 1]);
			}
			else if (p_92103_2_[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block)p_92103_2_[i + 1], 1, 32767);
			}
			else if (p_92103_2_[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack)p_92103_2_[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}

		ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, p_92103_1_);
		return shapedrecipes;
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
			log.info("Reloading Network");

			addressToController.clear();
			for(TileEntityStargateController cont : registering)
			{
				if(cont.getWorldObj().isRemote)
				{
					registering.remove();
					continue;
				}
				addressToController.put(cont.address, new Location(cont));
			}
		}
		catch(Throwable th){}
	}

	public static void addToRegister(TileEntityStargateController controller){
		try{
			for(TileEntityStargateController cont : registering)
				if(new Location(cont).equals(new Location(controller)))
					return;
			registering.add(controller);
		}
		catch(Throwable th){}
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

	public static void vaporiseEntity(Entity entity){
		if(entity.hurtResistantTime == 0)
		{
			NBTTagCompound inv = new NBTTagCompound();
			EntityPlayerMP player = null;
			if(entity instanceof EntityPlayerMP)
			{
				player = (EntityPlayerMP) entity;
				NBTTagList list = new NBTTagList();
				player.inventory.writeToNBT(list);
				inv.setTag("Inventory", list);
				inv.setInteger("TagType", list.func_150303_d());

				entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setTag("MineGate_Recovery", inv);
			}

			entity.setPosition(entity.posX, -128, entity.posZ);
			entity.attackEntityFrom(stargateDamage, Float.MAX_VALUE);
		}
	}

	public static HashMap<Integer, Teleporter> customTeleporters = new HashMap<Integer, Teleporter>();

	public static void teleportEntity(Entity teleporting, int dim){
		if(teleporting instanceof EntityPlayerMP) //minecraftserver.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) teleporting, dim, customTeleporters.get(dim));
		{
			EntityPlayerMP player = (EntityPlayerMP) teleporting;
			if (player.dimension == 1 && dim == 1)
			{
				player.triggerAchievement(AchievementList.theEnd2);
				player.worldObj.removeEntity(player);
				player.playerConqueredTheEnd = true;
				player.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
			}
			else
			{
				if (player.dimension == 0 && dim == 1)
				{
					player.triggerAchievement(AchievementList.theEnd);
					ChunkCoordinates chunkcoordinates = player.mcServer.worldServerForDimension(dim).getEntrancePortalLocation();

					if (chunkcoordinates != null)
					{
						player.playerNetServerHandler.setPlayerLocation((double)chunkcoordinates.posX, (double)chunkcoordinates.posY, (double)chunkcoordinates.posZ, 0.0F, 0.0F);
					}

					dim = 1;
				}
				else
				{
					player.triggerAchievement(AchievementList.portal);
				}

				if(!customTeleporters.containsKey(dim))
					customTeleporters.put(dim, new MinegateTeleporter(player.mcServer.worldServerForDimension(dim)));

				player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dim, customTeleporters.get(dim));
				//		        player.lastExperience = -1;
				//		        player.lastHealth = -1.0F;
				//		        player.lastFoodLevel = -1;
			}
		}
		else
			if (!teleporting.worldObj.isRemote && !teleporting.isDead)
			{
				teleporting.worldObj.theProfiler.startSection("changeDimension");
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				int j = teleporting.dimension;
				WorldServer worldserver = minecraftserver.worldServerForDimension(j);
				WorldServer worldserver1 = minecraftserver.worldServerForDimension(dim);

				if(!customTeleporters.containsKey(j))
					customTeleporters.put(j, new MinegateTeleporter(worldserver));

				if(!customTeleporters.containsKey(dim))
					customTeleporters.put(dim, new MinegateTeleporter(worldserver1));

				teleporting.dimension = dim;

				if (j == 1 && dim == 1)
				{
					worldserver1 = minecraftserver.worldServerForDimension(0);
					teleporting.dimension = 0;
				}

				teleporting.worldObj.removeEntity(teleporting);
				teleporting.isDead = false;
				teleporting.worldObj.theProfiler.startSection("reposition");
				minecraftserver.getConfigurationManager().transferEntityToWorld(teleporting, j, worldserver, worldserver1, customTeleporters.get(dim));
				teleporting.worldObj.theProfiler.endStartSection("reloading");
				Entity entity = EntityList.createEntityByName(EntityList.getEntityString(teleporting), worldserver1);

				if (entity != null)
				{
					entity.copyDataFrom(teleporting, true);

					if (j == 1 && dim == 1)
					{
						ChunkCoordinates chunkcoordinates = worldserver1.getSpawnPoint();
						chunkcoordinates.posY = teleporting.worldObj.getTopSolidOrLiquidBlock(chunkcoordinates.posX, chunkcoordinates.posZ);
						entity.setLocationAndAngles((double)chunkcoordinates.posX, (double)chunkcoordinates.posY, (double)chunkcoordinates.posZ, entity.rotationYaw, entity.rotationPitch);
					}

					worldserver1.spawnEntityInWorld(entity);
				}

				teleporting.isDead = true;
				teleporting.worldObj.theProfiler.endSection();
				worldserver.resetUpdateEntityTick();
				worldserver1.resetUpdateEntityTick();
				teleporting.worldObj.theProfiler.endSection();
			}
	}



	@SubscribeEvent
	public void onRespawn(PlayerRespawnEvent event){
		if(event.player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("MineGate_Recovery"))
		{
			ItemStack item = new ItemStack(recovery);
			item.setTagCompound(event.player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("MineGate_Recovery"));
			event.player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).removeTag("MineGate_Recovery");
			event.player.inventory.addItemStackToInventory(item);
		}
	}

}
