package com.benbenlaw.infinity.screen;

import com.benbenlaw.infinity.block.entity.InfinityGeneratorBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  https://github.com/BluSunrize/ImmersiveEngineering/blob/1.19.2/LICENSE
 *
 *  Slightly Modified Version by: Kaupenjoe
 */
public class GeneratorDisplayTooltipArea {

    private final InfinityGeneratorBlockEntity entity;

    public GeneratorDisplayTooltipArea(int xMin, int yMin, InfinityGeneratorBlockEntity entity)  {
        this(entity);
    }

    public GeneratorDisplayTooltipArea(InfinityGeneratorBlockEntity entity)  {
        this.entity = entity;
    }

    public List<Component> getTooltips() {

        System.out.println("getTooltips method called. Input: " + entity.getInput());
        System.out.println("getTooltips method called. RFPerTick: " + entity.getRFPerTick());
        System.out.println("getTooltips method called. power: " + entity.getMaxProgress());


        if (entity.getProgress() == 0 && entity.getMaxProgress() == 0) {
            return List.of(
                    Component.literal("Generator not running!"),
                    Component.literal("Check structure and fuel!")
            );
        }

        if (entity.getInput() != null) {
            return List.of(
                    Component.literal(entity.getInput().getDisplayName().getString() + " is producing "+ (entity.getMaxProgress()) * entity.getRFPerTick() +" FE"),
                    Component.literal("at " + (entity.getRFPerTick()) + "RF Per Tick")
            );
        }

        return List.of(
                Component.literal("null item"),
                Component.literal("at " + (entity.getRFPerTick()) + "RF Per Tick")

        );
    }

}