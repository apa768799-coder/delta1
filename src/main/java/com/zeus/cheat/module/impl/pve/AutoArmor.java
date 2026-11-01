package com.zeus.cheat.module.impl.pve;

import com.zeus.cheat.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {

    public AutoArmor() {
        super("AutoArmor");
    }

    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            
            if (stack.getItem() instanceof ArmorItem armor) {
                EquipmentSlot equipSlot = armor.getSlotType();
                int armorSlotId = equipSlot.getEntitySlotId();
                
                ItemStack currentArmor = mc.player.getEquippedStack(equipSlot);

                if (shouldEquip(stack, currentArmor)) {
                    int targetSlot = 36 + armorSlotId;
                    
                    if (i < 9) {
                        mc.interactionManager.clickSlot(
                                mc.player.playerScreenHandler.syncId,
                                targetSlot,
                                i,
                                SlotActionType.SWAP,
                                mc.player
                        );
                    } else {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, targetSlot, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    }
                    return;
                }
            }
        }
    }

    private int getProtection(ArmorItem armor) {
        ArmorItem.Type type = switch (armor.getSlotType()) {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
            default -> null;
        };
        
        if (type == null) return 0;
        return armor.getMaterial().getProtection(type);
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
