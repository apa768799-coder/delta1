package com.zeus.cheat.module.impl.pve;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(name = "AutoArmor", category = Category.PVE, description = "Equips best armor")
public class AutoArmor extends Module {
    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null) return;
        
        for (int i = 0; i < 4; i++) {
            ItemStack current = mc.player.getInventory().getArmorStack(i);
            int bestSlot = -1;
            int bestVal = current.isEmpty() ? -1 : getDefenseValue(current);

            for (int j = 9; j < 45; j++) {
                ItemStack stack = mc.player.getInventory().getStack(j);
                if (stack.getItem() instanceof ArmorItem armor) {
                    // Исправлено: getType().getId() вместо getSlotType()
                    if (armor.getType().getId() == i) { 
                        int val = getDefenseValue(stack);
                        if (val > bestVal) {
                            bestVal = val;
                            bestSlot = j;
                        }
                    }
                }
            }
            
            if (bestSlot != -1) {
                int targetSlot = 3 - i + 36; 
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bestSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, targetSlot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bestSlot, 0, SlotActionType.PICKUP, mc.player);
            }
        }
    }

    private int getDefenseValue(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armor) {
            // Исправлено: getProtection() вместо getDefense() или getProtectionAmount()
            return armor.getMaterial().getProtection(); 
        }
        return 0;
    }
}
