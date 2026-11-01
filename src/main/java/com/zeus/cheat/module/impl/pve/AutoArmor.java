package com.zeus.cheat.module.impl.pve;

// Убедитесь, что импорты соответствуют вашей базе (Module, Category)
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {

    // Конструктор, если в вашей базе он отличается — подправьте
    public AutoArmor() {
        super("AutoArmor", Category.PVE);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        // Проходим по слотам инвентаря (0-35)
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            
            if (stack.getItem() instanceof ArmorItem armor) {
                // ИСПРАВЛЕНИЕ 1: Используем getEquipmentSlot().getEntitySlotId() вместо getId()
                int armorSlotId = armor.getType().getEquipmentSlot().getEntitySlotId();
                
                EquipmentSlot equipSlot = armor.getType().getEquipmentSlot();
                ItemStack currentArmor = mc.player.getEquippedStack(equipSlot);

                // Если новая броня лучше текущей — надеваем
                if (shouldEquip(stack, currentArmor)) {
                    // Слоты брони в инвентаре: 36 (ботинки), 37 (штаны), 38 (нагрудник), 39 (шлем)
                    int targetSlot = 36 + armorSlotId;
                    
                    // Если предмет в хотбаре (0-8), используем SWAP, иначе PICKUP
                    if (i < 9) {
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                targetSlot,
                                i, // Индекс хотбара для SWAP
                                SlotActionType.SWAP,
                                mc.player
                        );
                    } else {
                        // Для предметов из основного инвентаря - двойной клик (PICKUP)
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

    // ИСПРАВЛЕНИЕ 2: Убираем getMaterial().getProtection(), так как в 1.21+ метод вызывается напрямую у предмета
    private int getProtection(ArmorItem armor) {
        return armor.getProtection();
    }

    // Вспомогательный метод для сравнения брони
    private boolean shouldEquip(ItemStack newArmorStack, ItemStack currentArmorStack) {
        if (currentArmorStack.isEmpty()) return true;
        if (!(currentArmorStack.getItem() instanceof ArmorItem currentArmorItem)) return true;

        ArmorItem newArmorItem = (ArmorItem) newArmorStack.getItem();

        int newProt = getProtection(newArmorItem);
        int currentProt = getProtection(currentArmorItem);

        return newProt > currentProt;
    }
}
