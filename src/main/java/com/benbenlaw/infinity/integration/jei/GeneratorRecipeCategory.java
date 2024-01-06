package com.benbenlaw.infinity.integration.jei;

import com.benbenlaw.infinity.Infinity;
import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.recipe.GeneratorRecipe;
import com.benbenlaw.infinity.recipe.ModRecipes;
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

public class GeneratorRecipeCategory implements IRecipeCategory<GeneratorRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Infinity.MOD_ID, "generator");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Infinity.MOD_ID, "textures/gui/jei_infinity.png");

    static final RecipeType<GeneratorRecipe> RECIPE_TYPE = RecipeType.create(Infinity.MOD_ID, "generator",
            GeneratorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public GeneratorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 40);
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
        builder.addSlot(RecipeIngredientRole.INPUT, 158, 2).addItemStack(recipe.getInputItem());
    }

    @Override
    public void draw(GeneratorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        @Nonnull final Minecraft minecraft = Minecraft.getInstance();

        int fuelDuration = recipe.getFuelDuration();
        String id = recipe.getPattern().replace("infinity:", "");
        int rfPerTick = recipe.getRFPerTick();

        guiGraphics.drawString(minecraft.font.self(), Component.translatable("jei_pattern." + id), 2, 0, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(minecraft.font.self(), Component.literal("Duration: " + fuelDuration + "T/" + fuelDuration/20 + "s"), 2, 11, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(minecraft.font.self(), Component.literal(rfPerTick + "RF Per Tick/" + rfPerTick*20 + " Per Second"), 2, 22, Color.darkGray.getRGB(), false);
        guiGraphics.drawString(minecraft.font.self(), Component.literal("Total RF Generated: " + (rfPerTick * fuelDuration) + " RF"), 2, 33, Color.darkGray.getRGB(), false);
    }
}
