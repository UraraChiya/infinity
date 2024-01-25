package com.benbenlaw.infinity.item;

import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.multiblock.MultiBlockManagers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Rotation;
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
                    boolean structureFound = false;

                    // Iterate over each rotation
                    for (Rotation rotation : Rotation.values()) {
                        var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, pos, rotation);
                        if (result != null) {
                            player.sendSystemMessage(Component.literal("Found Structure : %s (Rotation: %s)".formatted(result.ID(), rotation.toString())));
                            structureFound = true;
                            break; // Exit loop if structure is found
                        }
                    }

                    if (!structureFound) {
                        player.sendSystemMessage(Component.literal("Found no structure!"));
                    }
                }
            } else {
                assert player != null;
                player.sendSystemMessage(Component.literal("Right click on an Infinity Generator to check structure!"));
            }
        }
        return super.useOn(context);
    }

}
