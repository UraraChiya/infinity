package com.benbenlaw.infinity.item;

import com.benbenlaw.infinity.Infinity;
import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Infinity.MOD_ID);

    public static final RegistryObject<CreativeModeTab> INFINITY_TAB = CREATIVE_MODE_TABS.register("infinity", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.INFINITY_GENERATOR.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.infinity"))
            .displayItems((parameters, output) -> {

                output.accept(ModBlocks.INFINITY_GENERATOR.get());
                output.accept(ModItems.DETECTOR.get());
                output.accept(ModItems.HEAT_COIL.get());
                output.accept(ModItems.WOODEN_TURBINE.get());
                output.accept(ModItems.IRON_TURBINE.get());
                output.accept(ModItems.DIAMOND_TURBINE.get());


            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }


}
