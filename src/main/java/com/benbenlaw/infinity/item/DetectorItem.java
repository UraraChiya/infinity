package com.benbenlaw.infinity.item;

import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.multiblock.MultiBlockManagers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DetectorItem extends Item {
    public DetectorItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            var level = context.getLevel();
            var pos = context.getClickedPos();
            var player = context.getPlayer();

            if (level.getBlockState(pos).is(ModBlocks.INFINITY_GENERATOR.get())) {

                if (player != null) {
                    var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, pos);
                    if (result != null) {
                        player.sendSystemMessage(Component.literal("Found Structure : %s".formatted(result.ID())));

                    } else {
                        player.sendSystemMessage(Component.literal("Found no structure!"));
                    }
                }
            } else {
                assert player != null;
                player.sendSystemMessage(Component.literal("Right click on a Infinity Generator to check structure!"));
            }
        }
        return super.useOn(context);
    }
}
