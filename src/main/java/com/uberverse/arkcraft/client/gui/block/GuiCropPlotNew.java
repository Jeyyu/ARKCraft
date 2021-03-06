package com.uberverse.arkcraft.client.gui.block;

import com.uberverse.arkcraft.common.container.block.ContainerCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCropPlotNew extends GuiContainer
{
	private TileEntityCropPlot te;

	public GuiCropPlotNew(InventoryPlayer inventory, TileEntityCropPlot tileEntity)
	{
		super(new ContainerCropPlot(inventory, tileEntity));
		this.te = tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(
				"arkcraft:textures/gui/crop_plot_gui_new.png"));
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = I18n.format("tile.crop_plot.name");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		// fontRendererObj.drawString(
		// I18n.format("arkcraft.water", I18n.format("tile.water.name"),
		// (te.getField(0) / 20) + "/" + te.getType().getMaxWater() / 20,
		// te.getField(0) > 0 ? I18n.format("arkcraft.cropPlotWater.irrigated")
		// : I18n
		// .format("arkcraft.cropPlotWater.notIrrigated")),
		// 8, 15, te.getField(0) < 1 ? 0xFF0000 : 4210752);
		// fontRendererObj.drawString(
		// I18n.format("arkcraft.gui.fertilizer", te.getField(2) /
		// (te.getField(-20) + 1)), 8,
		// 63, te.getField(1) < 1 ? 0xFF0000 : 4210752);
	}
	//
	// @Override
	// public void updateScreen()
	// {
	// super.updateScreen();
	// int f = te.getField(1) / 20;
	// for (int i = 0; i < 10; i++)
	// {
	// if (te.getStack()[i] != null)
	// {
	// Item item = te.getStack()[i].getItem();
	// if (item instanceof ARKCraftFeces)
	// {
	// f += (te.getStack()[i].getMaxDamage() -
	// te.getStack()[i].getItemDamage());
	// }
	// }
	// }
	// te.setField(2, f);
	// }
}
