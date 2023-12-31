package com.benbenlaw.infinity;

import com.benbenlaw.infinity.block.ModBlocks;
import com.benbenlaw.infinity.block.entity.ModBlockEntities;
import com.benbenlaw.infinity.config.ConfigFile;
import com.benbenlaw.infinity.item.ModCreativeTab;
import com.benbenlaw.infinity.item.ModItems;
import com.benbenlaw.infinity.networking.ModMessages;
import com.benbenlaw.infinity.screen.InfinityGeneratorScreen;
import com.benbenlaw.infinity.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Infinity.MOD_ID)
public class Infinity {

    public static final String MOD_ID = "infinity";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Infinity() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeTab.register(eventBus);

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        eventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigFile.SPEC, "infinity.toml");

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getName());
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("com", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            MenuScreens.register(ModMenuTypes.INFINITY_GENERATOR_MENU.get(), InfinityGeneratorScreen::new);

        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus =Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
          //      ModItemProperties.addCustomItemProperties();
           //     ModItemProperties.addCustomItemProperties();
            });
        }
    }




}
