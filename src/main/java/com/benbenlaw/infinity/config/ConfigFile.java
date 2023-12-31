package com.benbenlaw.infinity.config;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ConfigFile {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

 //   public static final ForgeConfigSpec.ConfigValue<String> itemStack;
//    public static final ForgeConfigSpec.ConfigValue<Integer> duration;


    static {
        BUILDER.push("Infinity Config File");

        /*
        itemStack = BUILDER.comment("Item generated from collector")
                .define("Item from Collector", "minecraft:diamond");

        duration = BUILDER.comment("Time in ticks for the collector")
                .define("Time in ticks for the collector", 20);

         */


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
