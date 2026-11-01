package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class ToolUtils {
    // Находит слот в хотбаре с самым быстрым инструментом для конкретного блока
    public static int findBestTool(BlockState state) {
        float bestSpeed = ZeusClient.mc.player.getInventory().getMainHandStack().getMiningSpeedMultiplier(state);
        int bestSlot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = ZeusClient.mc.player.getInventory().getStack(i);
            float speed = stack.getMiningSpeedMultiplier(state);
            
            // Проверяем, что инструмент действительно быстрее текущего и может добыть блок (isSuitableFor)
            if (speed > bestSpeed && stack.isSuitableFor(state)) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }
}
