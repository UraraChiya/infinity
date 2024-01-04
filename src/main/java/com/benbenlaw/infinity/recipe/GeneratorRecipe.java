package com.benbenlaw.infinity.recipe;

import com.benbenlaw.infinity.Infinity;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GeneratorRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final String pattern;
    private final ItemStack inputItem;
    private final int fuelDuration;
    private final int RFPerTick;

    public GeneratorRecipe(ResourceLocation id, String pattern, ItemStack inputItem, int fuelDuration, int RFPerTick) {
        this.id = id;
        this.pattern = pattern;
        this.inputItem = inputItem;
        this.fuelDuration = fuelDuration;
        this.RFPerTick = RFPerTick;
    }

    public String getPattern() {
        return pattern;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public int getFuelDuration() {
        return fuelDuration;
    }

    public int getRFPerTick() {
        return RFPerTick;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer inventory, @NotNull Level pLevel) {

        if(inputItem.is(inventory.getItem(0).getItem().asItem())) {
            return true;
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer inv, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GeneratorRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return GeneratorRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<GeneratorRecipe> {
        private Type() { }
        public static final GeneratorRecipe.Type INSTANCE = new GeneratorRecipe.Type();
        public static final String ID = "generator";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<GeneratorRecipe> {
        public static final GeneratorRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Infinity.MOD_ID, "generator");

        @Override
        public GeneratorRecipe fromJson(ResourceLocation id, JsonObject json) {

            String pattern = GsonHelper.getAsString(json, "pattern");
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"input"));
            int fuelDuration = GsonHelper.getAsInt(json, "fuel_duration");
            int RFPerTick = GsonHelper.getAsInt(json, "rf_per_tick");

            return new GeneratorRecipe(id, pattern, input, fuelDuration, RFPerTick);
        }

        @Override
        public GeneratorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String pattern = buf.readUtf();
            ItemStack input = buf.readItem();
            int fuelDuration = buf.readInt();
            int RFPerTick = buf.readInt();

            return new GeneratorRecipe(id, pattern, input, fuelDuration, RFPerTick);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GeneratorRecipe recipe) {

            buf.writeUtf(recipe.getPattern(), Short.MAX_VALUE);
            buf.writeItemStack(recipe.inputItem, false);
            buf.writeInt(recipe.getFuelDuration());
            buf.writeInt(recipe.getRFPerTick());
        }
    }
}
