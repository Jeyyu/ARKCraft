/**
 * 
 */
package com.uberverse.arkcraft.deprecated;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ERBF | Aug 9, 2016
 *
 */
public class Engram extends Item
{

	private String name;
	private int unlockLevel, ep;
	private boolean hasLearned;

	public Engram(String name, int unlockLevel, int ep)
	{
		super();
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);

		this.name = name;
		this.unlockLevel = unlockLevel;
		this.ep = ep;

		GameRegistry.registerItem(this, name);
		ARKCraftEngrams.engramList.add(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		tooltip.add(hasLearned ? EnumChatFormatting.BLUE + I18n.format(
				"ark.engram.learn.true") + ":" : I18n.format("ark.engram.learn.false") + ":");
		tooltip.add(I18n.format("item." + name + ".name"));
		tooltip.add(I18n.format("ark.engram.consume") + ": " + ep);
	}

	public void setLearned(boolean learned)
	{
		hasLearned = learned;
	}

	public void setLearned()
	{
		setLearned(true);
	}

	public void setUnlearned()
	{
		setLearned(false);
	}

	public int getUnlockLevel()
	{
		return unlockLevel;
	}

	public int getRequiredPoints()
	{
		return ep;
	}

	public String getFormattedName()
	{
		return I18n.format("item." + name + ".name");
	}

	public String getName()
	{
		return name;
	}

	public String getFormattedDesc()
	{
		return I18n.format("engram." + name + ".desc");
	}

	public String getDesc()
	{
		return "engram." + name + ".desc";
	}

}