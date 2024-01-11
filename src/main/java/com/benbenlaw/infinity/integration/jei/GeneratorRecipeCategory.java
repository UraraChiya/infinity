package com.benbenlaw.infinity.integration.jei;

import com.benbenlaw.infinity.Infinity;
import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.recipe.GeneratorRecipe;
import com.benbenlaw.infinity.recipe.ModRecipes;
import com.benbenlaw.infinity.util.MouseUtil;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Optional;

public class GeneratorRecipeCategory implements IRecipeCategory<GeneratorRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Infinity.MOD_ID, "generator");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Infinity.MOD_ID, "textures/gui/jei_infinity.png");

    static final RecipeType<GeneratorRecipe> RECIPE_TYPE = RecipeType.create(Infinity.MOD_ID, "generator",
            GeneratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public GeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.INFINITY_GENERATOR.get()));
    }

    @Override
    public RecipeType<GeneratorRecipe> getRecipeType() {
        return new RecipeType<>(ModRecipes.GENERATOR_SERIALIZER.getId(), GeneratorRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.literal("Generator");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GeneratorRecipe recipe, @NotNull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 13).addItemStack(recipe.getInputItem());
    }

    @Override
    public void draw(GeneratorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int fuelDuration = recipe.getFuelDuration();
        String id = recipe.getPattern().replace("infinity:", "");
        int rfPerTick = recipe.getRFPerTick();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei_pattern." + id), 2, 0, Color.darkGray.getRGB(), false);

        if (!recipe.getInputItem().isDamageableItem()) {
            guiGraphics.drawString(minecraft.font.self(), Component.literal("Uses Per Item: " + "1"), 68, 43, Color.darkGray.getRGB(), false);
        } else {
            guiGraphics.drawString(minecraft.font.self(), Component.literal("Uses Per Item: " + recipe.getInputItem().getMaxDamage()), 68, 43, Color.darkGray.getRGB(), false);

        }
        boolean durationArea = mouseX >= 67 && mouseX <= 171 && mouseY >= 16 && mouseY <= 25;
        boolean rfArea = mouseX >= 67 && mouseX <= 171 && mouseY >= 29 && mouseY <= 38;

        if (durationArea) {
            guiGraphics.drawString(minecraft.font.self(), Component.literal( fuelDuration/20 + " Seconds"), 68, 17, Color.darkGray.getRGB(), false);
        } else {
            guiGraphics.drawString(minecraft.font.self(), Component.literal(fuelDuration + " Ticks"), 68, 17, Color.darkGray.getRGB(), false);

        }
        if (rfArea) {
            guiGraphics.drawString(minecraft.font.self(), Component.literal( "RF Per Tick: " + rfPerTick), 68, 30, Color.white.getRGB(), false);
        } else {
            guiGraphics.drawString(minecraft.font.self(), Component.literal("Total RF: " + (rfPerTick * fuelDuration) + " RF"), 68, 30, Color.white.getRGB(), false);

        }

    }
}
