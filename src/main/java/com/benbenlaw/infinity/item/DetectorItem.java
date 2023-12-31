package com.benbenlaw.infinity.item;

import com.benbenlaw.infinity.multiblock.MultiBlockManagers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class DetectorItem extends Item {
    public DetectorItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            var level = context.getLevel();
            var pos = context.getClickedPos();
            var player = context.getPlayer();
            if (player != null) {
                var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, pos);
                if (result != null) {
                    player.sendSystemMessage(Component.literal("Found Structure : %s".formatted(result.ID())));
                } else {
                    player.sendSystemMessage(Component.literal("Found no structure!"));
                }
            }
        }
        return super.useOn(context);
    }
}
