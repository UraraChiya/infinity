package com.benbenlaw.infinity.recipe;

import com.benbenlaw.infinity.Infinity;
import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER=
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Infinity.MOD_ID);


    public static final RegistryObject<RecipeSerializer<GeneratorRecipe>> GENERATOR_SERIALIZER =
            SERIALIZER.register("generator", () -> GeneratorRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZER.register(eventBus);
    }


}
