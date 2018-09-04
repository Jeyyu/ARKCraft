package com.arkcraft.client.gui.overlay;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.event.CommonEventHandler;
import com.arkcraft.common.item.ranged.ItemRangedWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ARKCraft.MODID, value = Side.CLIENT)
public class GUIOverlayReloading {
	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public static void renderGUIOverlay(RenderGameOverlayEvent.Post e) {
		EntityPlayer p = mc.player;
		ItemStack stack = p.getHeldItemMainhand();
		if (e.getType().equals(ElementType.HOTBAR)) {
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
				ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
				boolean rld = weapon.isLoaded(stack, p);
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glDisable(GL11.GL_LIGHTING);
				int x0 = e.getResolution().getScaledWidth() / 2 - 88 + p.inventory.currentItem * 20;
				int y0 = e.getResolution().getScaledHeight() - 3;
				float f;
				int color;
				if (rld) {
					f = 1F;
					if (p.getActiveItemStack() == stack) {
						color = 0x60C60000;
					} else {
						color = 0x60348E00;
					}

				} else if (weapon.isReloading(stack)) {
					f = Math.min((float) CommonEventHandler.reloadTicks / weapon.getReloadDuration(), 1F);
					color = 0x60EAA800;
				} else {
					f = 0F;
					color = 0;
				}
				Gui.drawRect(x0, y0, x0 + 16, y0 - (int) (f * 16), color);
			}
		} else if (e.getType().equals(ElementType.CROSSHAIRS)) {
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
				String text = "";
				if (!p.capabilities.isCreativeMode) {
					ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
					text = weapon.getAmmoQuantity(stack) + "/" + weapon.getAmmoQuantityInInventory(stack, p);
				} else {
					text = '\u221e' + "";
				}
				int x = e.getResolution().getScaledWidth() - 4 - mc.fontRenderer.getStringWidth(text);
				int y = 20;
				mc.currentScreen.drawString(mc.fontRenderer, text, x, y - 16, 0xFFFFFFFF);
			}
		}
	}
}