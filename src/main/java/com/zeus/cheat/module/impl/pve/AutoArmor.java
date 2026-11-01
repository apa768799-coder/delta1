package com.zeus.cheat.module.impl.pve;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(name = "AutoArmor", category = Category.PVE, description = "Equips the best armor automatically")
public class AutoArmor extends Module {
    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null) return;
        
        for (int i = 0; i < 4; i++) {
            ItemStack current = mc.player.getInventory().getArmorStack(i);
            int bestSlot = -1;
            int bestValue = current.isEmpty() ? -1 : ((ArmorItem) current.getItem()).getMaterial().getProtectionAmount();

            for (int j = 9; j < 45; j++) {
                ItemStack stack = mc.player.getInventory().getStack(j);
                if (stack.getItem() instanceof ArmorItem armor) {
                    if (armor.getSlotType().getId() == i) {
                        int value = armor.getMaterial().getProtectionAmount();
                        if (value > bestValue) {
                            bestValue = value;
                            bestSlot = j;
                        }
                    }
                }
            }
            
            if (bestSlot != -1) {
                int targetSlot = 3 - i + 36; // Map armor slot to hotbar slot for swapping
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bestSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, targetSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bestSlot, 0, SlotActionType.PICKUP, mc.player);
            }
        }
    }
}
