package com.arkcraft.common.network;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.inventory.InventoryAttachment;
import com.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.util.SoundUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class GunFired implements IMessage {
	public GunFired() {

	}

	static void processMessage(GunFired message, EntityPlayerMP player) {
		if (player != null) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack != null && stack.getItem() instanceof ItemRangedWeapon) {
				ItemRangedWeapon weapon = (ItemRangedWeapon) stack.getItem();
				String soundPath = ARKCraft.MODID + ":" + weapon.getTranslationKey() + "_shoot";
				weapon.fire(stack, player.world, player, 0);
				player.setActiveHand(player.getActiveHand());
				InventoryAttachment att = InventoryAttachment.create(stack);
				if (att != null && att.isSilencerPresent())
					soundPath = soundPath + "_silenced";
				//weapon.setFired(stack, player, true);
				SoundUtil.playSound(Minecraft.getMinecraft().world, player.posX, player.posY, player.posZ, new ResourceLocation(soundPath), SoundCategory.PLAYERS, 1.5F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.7F), false);
			}
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<GunFired, IMessage> {
		@Override
		public IMessage onMessage(final GunFired message, MessageContext ctx) {
			if (ctx.side != Side.SERVER) {
				System.err.println("MPUpdateDoReloadStarted received on wrong side:" + ctx.side);
				return null;
			}
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServer().addScheduledTask(() -> processMessage(message, player));
			return null;
		}
	}

}