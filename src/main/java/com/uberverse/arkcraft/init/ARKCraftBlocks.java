package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.ARKCraftBerryBush;
import com.uberverse.arkcraft.common.block.BlockGreenScreen;
import com.uberverse.arkcraft.common.block.BlockLight;
import com.uberverse.arkcraft.common.block.crafter.BlockCampfire;
import com.uberverse.arkcraft.common.block.crafter.BlockCompostBin;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot;
import com.uberverse.arkcraft.common.block.crafter.BlockMortarAndPestle;
import com.uberverse.arkcraft.common.block.crafter.BlockRefiningForge;
import com.uberverse.arkcraft.common.block.crafter.BlockSmithy;
import com.uberverse.arkcraft.common.item.itemblock.ItemCampfire;
import com.uberverse.arkcraft.common.item.itemblock.ItemCompostBin;
import com.uberverse.arkcraft.common.item.itemblock.ItemCropPlot;
import com.uberverse.arkcraft.common.item.itemblock.ItemMortarAndPestle;
import com.uberverse.arkcraft.common.item.itemblock.ItemRefiningForge;
import com.uberverse.arkcraft.common.item.itemblock.ItemSmithy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileInventoryCompostBin;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityMP;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;
import com.uberverse.arkcraft.wip.oregen.BlockRockResource;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftBlocks
{
	public static BlockLight block_light;
	public static ARKCraftBerryBush berryBush;
	public static BlockCompostBin compost_bin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle pestle;
	public static BlockCropPlot crop_plot;
	public static BlockRefiningForge refining_forge;
	public static BlockCampfire campfire;
	public static BlockGreenScreen greenScreen;

	public static BlockRockResource rockResource;

	public static ARKCraftBlocks getInstance()
	{
		return new ARKCraftBlocks();
	}

	public static void init()
	{
		InitializationManager init = InitializationManager.instance();

		// Misc
		block_light = init.registerBlock("block_light", new BlockLight());
		berryBush = init.registerBlock("berryBush", new ARKCraftBerryBush(0.4F));
		greenScreen = init.registerBlock("greenScreen", new BlockGreenScreen());

		// Containers
		smithy = init.registerBlock("smithy", new BlockSmithy(), ItemSmithy.class);
		pestle = init.registerBlock("mortar_and_pestle", new BlockMortarAndPestle(), ItemMortarAndPestle.class);
		crop_plot = init.registerBlock("crop_plot", new BlockCropPlot(), ItemCropPlot.class);
		compost_bin = init.registerBlock("compost_bin", new BlockCompostBin(), ItemCompostBin.class);
		refining_forge = init.registerBlock("refining_forge", new BlockRefiningForge(), ItemRefiningForge.class);
		campfire = init.registerBlock("campfire", new BlockCampfire(), ItemCampfire.class);

		rockResource = init.registerBlock("rock_resource", new BlockRockResource());

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlot.class, ARKCraft.MODID + "cropPlot");
		GameRegistry.registerTileEntity(TileEntityMP.class, ARKCraft.MODID + "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, ARKCraft.MODID + "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileEntitySmithy.class, ARKCraft.MODID + "TileEntitySmithy");
		GameRegistry.registerTileEntity(TileEntityRefiningForge.class, ARKCraft.MODID + "TileEntityRefiningForge");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ARKCraft.MODID + "TileEntityCampfire");
	}
}
