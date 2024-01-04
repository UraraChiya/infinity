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
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private final InfinityGeneratorBlockEntity entity;

    public GeneratorDisplayTooltipArea(int xMin, int yMin, InfinityGeneratorBlockEntity entity)  {
        this(xMin, yMin, entity,8,64);
    }

    public GeneratorDisplayTooltipArea(int xMin, int yMin, InfinityGeneratorBlockEntity entity, int width, int height)  {
        xPos = xMin;
        yPos = yMin;
        this.width = width;
        this.height = height;
        this.entity = entity;
    }

    public List<Component> getTooltips() {

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

    public void render(GuiGraphics guiGraphics) {
        int stored = (int)(height * (entity.getEnergyStorage().getEnergyStored() / (float)entity.getEnergyStorage().getMaxEnergyStored()));
        guiGraphics.fillGradient(xPos,yPos + (height - stored),xPos + width,
                yPos + height,0xffb51500, 0xff600b00);
    }
}