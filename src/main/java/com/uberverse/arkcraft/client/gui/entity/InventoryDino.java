package com.uberverse.arkcraft.client.gui.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryDino extends InventoryBasic
{

    public InventoryDino(String title, boolean customName, int slotCount)
    {
        super(title, customName, slotCount);
    }

    public void loadInventoryFromNBT(NBTTagList nbt)
    {
        int i;
        for (i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, null);
        }
        for (i = 0; i < nbt.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
    }

    public void saveInventoryToNBT(NBTTagCompound nbt)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        nbt.setTag("Items", nbttaglist);
    }

    public List<ItemStack> getInventoryStacks()
    {
        List<ItemStack> stacks = Lists.newArrayList();

        for (int i = 0; i < getSizeInventory(); i++)
            stacks.add(getStackInSlot(i));

        return stacks;
    }
}