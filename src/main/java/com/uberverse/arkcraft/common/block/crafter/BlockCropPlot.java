package com.uberverse.arkcraft.common.block.crafter;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.CropPlotType;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.Part;
import com.uberverse.arkcraft.util.Identifiable;
import com.uberverse.arkcraft.util.Utils;

/**
 * @author wildbill22
 */
public class BlockCropPlot extends BlockContainer implements Identifiable
{
	public static final int GROWTH_STAGES = 4; // 0 - 4
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, GROWTH_STAGES);
	public static final PropertyEnum<CropPlotType> TYPE = PropertyEnum.create("type", CropPlotType.class);
	public static final PropertyEnum<BerryColor> BERRY = PropertyEnum.create("berry", BerryColor.class);
	public static final PropertyBool TRANSPARENT = PropertyBool.create("transparent");
	private EnumBlockRenderType renderType = EnumBlockRenderType.MODEL; // default

	public BlockCropPlot()
	{
		super(Material.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		// this.setTickRandomly(true);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setHardness(0.5F);
		translucent = true;
		lightOpacity = 0;
		this.disableStats();
	}

	// replace use with below in comment? -> less tileentities means less lag?
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCropPlot();
	}

	// @Override
	// public TileEntity createTileEntity(World world, IBlockState state)
	// {
	// return (boolean) state.getValue(TRANSPARENT) ? null : new
	// TileEntityCropPlot();
	// }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		state = getActualState(state, worldIn, pos);
		if (!playerIn.isSneaking()) {
			if (state.getValue(TYPE) != TileEntityCropPlot.CropPlotType.SMALL && state.getValue(TRANSPARENT)) {
				for (int x = -1; x < 2; x++)
					for (int z = -1; z < 2; z++) {
						BlockPos pos2 = pos.add(x, 0, z);
						if (!(x == pos.getX() && z == pos.getZ()) && worldIn.getBlockState(pos2).getBlock() instanceof BlockCropPlot) {
							IBlockState s = getActualState(worldIn.getBlockState(pos2), worldIn, pos2);
							if (!(boolean) s.getValue(TRANSPARENT))
								return this.onBlockActivated(worldIn, pos2, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
						}
					}
			}
			else {
				// TileEntityCropPlot te = (TileEntityCropPlot)
				// worldIn.getTileEntity(pos);
				// if (te.part != Part.MIDDLE)
				// {
				// BlockPos pos2 = te.part.offset(pos, true);
				// return this.onBlockActivated(worldIn, pos2,
				// worldIn.getBlockState(pos2), playerIn, side, hitX, hitY,
				// hitZ);
				// }
				// else
				// {
				if (playerIn.getHeldItem(hand) != null && playerIn.getHeldItem(hand).getItem() instanceof ARKCraftSeed && (state.getValue(AGE)) == 0) {
					ItemStack s = playerIn.getHeldItem(hand).splitStack(1);
					s = TileEntityHopper.putStackInInventoryAllSlots((IInventory) worldIn.getTileEntity(pos), s, null);
					if (s != null) {
						if (!playerIn.inventory.addItemStackToInventory(s)) {
							EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), s);
							worldIn.spawnEntity(item);
						}
					}
				}
				// }
				TileEntity entity = worldIn.getTileEntity(pos);
				if (entity instanceof TileEntityCropPlot && entity != null) {
					TileEntityCropPlot target = (TileEntityCropPlot) entity;
					if(!Utils.interactWithFluidHandler(target.getWater(), playerIn, hand)){
						playerIn.openGui(ARKCraft.instance(), getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
					}
				}
			}
			return true;
		}
		return false;
	}

	public void setRenderType(EnumBlockRenderType renderType)
	{
		this.renderType = renderType;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return renderType;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta % 5)).withProperty(TYPE, CropPlotType.VALUES[meta / 5]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE).intValue() + state.getValue(TYPE).ordinal() * 5;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AGE, TYPE, BERRY, TRANSPARENT);
	}

	/**
	 * Returns randomly, about 1/2 of the fertilizer and berries
	 */
	/*
	 * @Override public java.util.List<ItemStack>
	 * getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos,
	 * IBlockState state, int fortune) { java.util.List<ItemStack> ret =
	 * super.getDrops(world, pos, state, fortune); Random rand = world
	 * instanceof World ? ((World) world).rand : new Random(); TileEntity
	 * tileEntity = world.getTileEntity(pos); if (tileEntity instanceof
	 * TileInventoryCropPlot) { TileInventoryCropPlot tiCropPlot =
	 * (TileInventoryCropPlot) tileEntity; for (int i = 0; i <
	 * TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT; ++i) { if (rand.nextInt(2)
	 * == 0) { ret.add(tiCropPlot
	 * .getStackInSlot(TileInventoryCropPlot.FIRST_FERTILIZER_SLOT + i)); } } if
	 * (rand.nextInt(2) == 0) {
	 * ret.add(tiCropPlot.getStackInSlot(TileInventoryCropPlot.BERRY_SLOT)); } }
	 * return ret; }
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityCropPlot tile = (TileEntityCropPlot) worldIn.getTileEntity(pos);
		if (tile != null)
			InventoryHelper.dropInventoryItems(worldIn, pos, tile);
		CropPlotType t = state.getValue(TYPE);
		if (tile.part == Part.MIDDLE) {
			if (t != CropPlotType.SMALL) {
				for (Part p : Part.VALUES) {
					if (p != Part.MIDDLE) {
						BlockPos pos2 = p.offset(pos, false);
						worldIn.setBlockToAir(pos2);
					}
				}
			}
		}
		else {
			worldIn.setBlockState(tile.part.offset(pos, true), Blocks.AIR.getDefaultState());
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		CropPlotType t = CropPlotType.VALUES[stack.getMetadata() % 3];
		state = getDefaultState();
		state = state.withProperty(TYPE, t);
		worldIn.setBlockState(pos, state);
		if (tile != null && tile instanceof TileEntityCropPlot) {
			tile.validate();
			worldIn.setTileEntity(pos, tile);
		}
		if (t != CropPlotType.SMALL) {
			for (Part p : Part.VALUES) {
				if (p != Part.MIDDLE) {
					BlockPos pos2 = p.offset(pos, false);
					worldIn.setBlockState(pos2, state);
					TileEntity tileNew = worldIn.getTileEntity(pos2);
					if (tileNew instanceof TileEntityCropPlot) {
						TileEntityCropPlot te = (TileEntityCropPlot) tileNew;
						te.part = p;
					}
					else {
						ARKCraft.logger.error("Invalid tile entity at " + pos2.toString() + " tile entity: " + tileNew);
					}
				}
			}
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
	{
		list.add(new ItemStack(itemIn, 1, 0));
		list.add(new ItemStack(itemIn, 1, 1));
		list.add(new ItemStack(itemIn, 1, 2));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(TYPE).ordinal();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		CropPlotType t = state.getValue(TYPE);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot) {
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL)
				return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ() + 1);
			else if (t == CropPlotType.MEDIUM)
				return new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p.getZ());
			else if (t == CropPlotType.LARGE)
				return new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ() - 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ());
		}
		else {
			return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ() + 1);
		}
		return super.getSelectedBoundingBox(state, worldIn, pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		AxisAlignedBB bb = null;
		CropPlotType t = state.getValue(TYPE);
		TileEntity tile = source.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot) {
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL)
				bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ() + 1);
			else if (t == CropPlotType.MEDIUM)
				bb = new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p.getZ());
			else if (t == CropPlotType.LARGE)
				bb = new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ() - 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ());
		}
		else {
			bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ() + 1);
		}

		return bb.offset(-pos.getX(), -pos.getY(), -pos.getZ());
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		CropPlotType t = state.getValue(TYPE);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot) {
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			BlockPos p = new BlockPos(0, 0, 0);
			p = te.part.offset(p, true);
			if (t == CropPlotType.SMALL)
				return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.35F, pos.getZ() + 1).offset(-pos.getX(), -pos.getY(), -pos.getZ());
			else if (t == CropPlotType.MEDIUM)
				return new AxisAlignedBB(pos.getX() - 0.5F + p.getX(), pos.getY(), pos.getZ() - 0.5F + p.getZ(), pos.getX() + 1.5F + p.getX(), pos.getY() + 0.35F, pos.getZ() + 1.5F + p.getZ()).offset(-pos.getX(), -pos.getY(), -pos.getZ());
			else if (t == CropPlotType.LARGE)
				return new AxisAlignedBB(pos.getX() - 1 + p.getX(), pos.getY(), pos.getZ() - 1 + p.getZ(), pos.getX() + 2 + p.getX(), pos.getY() + 0.35F, pos.getZ() + 2 + p.getZ()).offset(-pos.getX(), -pos.getY(), -pos.getZ());;
		}
		else {
			return FULL_BLOCK_AABB;
		}
		return FULL_BLOCK_AABB;
	}

	public static enum BerryColor implements IStringSerializable
	{
		AMAR, AZUL, MEJO, NARCO, STIM, TINTO

		;

		public static final BerryColor[] VALUES = values();

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityCropPlot) {
			TileEntityCropPlot te = (TileEntityCropPlot) tile;
			return state.withProperty(BERRY, te.getGrowingColor()).withProperty(TRANSPARENT, te.isTransparent());
		}
		return state;
	}

	@Override
	public void fillWithRain(World worldIn, BlockPos pos)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) worldIn.getTileEntity(pos);
		if (te.part == Part.MIDDLE) {
			te.fillWithRain(true);
		}
		else {
			TileEntity tile = worldIn.getTileEntity(te.part.offset(pos, true));
			if (tile instanceof TileEntityCropPlot) {
				te = (TileEntityCropPlot) tile;
				te.fillWithRain(true);
			}
		}
	}

	@Override
	public int getId()
	{
		return CommonProxy.GUI.CROP_PLOT.id;
	}
	@Override
	public boolean isFullBlock(IBlockState s) {
		return false;
	}
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 10;
	}
}
