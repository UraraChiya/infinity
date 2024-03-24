package com.benbenlaw.infinity.item;

import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.block.custom.InfinityGeneratorBlock;
import com.benbenlaw.infinity.multiblock.MultiBlockManagers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.NotNull;
import org.mangorage.mangomultiblock.core.misc.Util;

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

                    Direction direction = level.getBlockState(pos).getValue(InfinityGeneratorBlock.FACING);
                    Rotation rotation = Util.DirectionToRotation(direction);

                    // Iterate over each rotation
                    var result = MultiBlockManagers.POWER_MULTIBLOCKS.findStructure(level, pos, rotation);
                    if (result != null) {

                        String id = result.ID().replace("infinity:", "");
                        MutableComponent translatedID = Component.translatable("jei_pattern." + id);
                        player.sendSystemMessage(Component.literal("Found Pattern: " + translatedID.getString()).withStyle(ChatFormatting.GREEN));
                        structureFound = true;
                    }

                    if (!structureFound) {
                        player.sendSystemMessage(Component.literal("Found Pattern: None, Check Your Structure!").withStyle(ChatFormatting.RED));
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
