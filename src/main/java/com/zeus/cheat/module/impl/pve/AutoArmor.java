package com.zeus.cheat.module.impl.pve;

import com.zeus.cheat.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

// ВАЖНО: Если в вашем проекте используется аннотация для задания имени и категории,
// например @ModuleInfo(name = "AutoArmor", category = Category.PVE), раскомментируйте её.
// public class AutoArmor extends Module {
public class AutoArmor extends Module {

    public AutoArmor() {
        // Конструктор оставлен пустым, так как базовый класс Module не принимает аргументов.
    }

    // Если в базовом классе метод называется onTick(), замените onUpdate на onTick.
    // Если он вообще не переопределяется, просто оставьте как есть.
    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        // Проходим по слотам инвентаря (0-35)
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            
            if (stack.getItem() instanceof ArmorItem armor) {
                // ИСПРАВЛЕНИЕ 1: В 1.21.4 нет метода getId(), используем getEquipmentSlot().getEntitySlotId()
                int armorSlotId = armor.getType().getEquipmentSlot().getEntitySlotId();
                
                EquipmentSlot equipSlot = armor.getType().getEquipmentSlot();
                ItemStack currentArmor = mc.player.getEquippedStack(equipSlot);

                // Если новая броня лучше текущей — надеваем
                if (shouldEquip(stack, currentArmor)) {
                    // Слоты брони в инвентаре игрока: 36 (ботинки), 37 (штаны), 38 (нагрудник), 39 (шлем)
                    int targetSlot = 36 + armorSlotId;
                    
                    if (i < 9) { // Если предмет в хотбаре
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                targetSlot,
                                i, 
                                SlotActionType.SWAP,
                                mc.player
                        );
                    } else { // Если в основном инвентаре
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                i,
                                0,
                                SlotActionType.PICKUP,
                                mc.player
                        );
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                targetSlot,
                                0,
                                SlotActionType.PICKUP,
                                mc.player
                        );
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                i,
                                0,
                                SlotActionType.PICKUP,
                                mc.player
                        );
                    }
                    return; // Ждем следующий тик после смены брони
                }
            }
        }
    }

    // ИСПРАВЛЕНИЕ 2: Вызываем getProtection() напрямую у предмета
    private int getProtection(ArmorItem armor) {
        return armor.getProtection();
    }

    private boolean shouldEquip(ItemStack newArmorStack, ItemStack currentArmorStack) {
        if (currentArmorStack.isEmpty()) return true;
        if (!(currentArmorStack.getItem() instanceof ArmorItem currentArmorItem)) return true;

        ArmorItem newArmorItem = (ArmorItem) newArmorStack.getItem();

        int newProt = getProtection(newArmorItem);
        int currentProt = getProtection(currentArmorItem);

        return newProt > currentProt;
    }
}
