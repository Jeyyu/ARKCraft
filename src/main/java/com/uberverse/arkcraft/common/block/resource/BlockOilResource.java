package com.uberverse.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;

import net.minecraft.block.material.Material;

public class BlockOilResource extends BlockARKResource
{
	public BlockOilResource()
	{
		super(Material.CORAL);
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.oil, 2), new AbstractItemStack(ARKCraftItems.stone,
				3));
	}
}
