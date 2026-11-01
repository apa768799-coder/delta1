package com.zeus.cheat.module.impl.pve;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(name = "AutoTotem", category = Category.PVE, description = "Automatically equips totems in offhand")
public class AutoTotem extends Module {
    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null) return;
        if (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;

        int totemSlot = -1;
        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }

        if (totemSlot != -1) {
            int currentSlot = mc.player.getInventory().selectedSlot;
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, totemSlot, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, totemSlot, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, currentSlot, -2, SlotActionType.PICKUP, mc.player);
        }
    }
}
