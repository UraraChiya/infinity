package com.benbenlaw.infinity.multiblock;

import com.benbenlaw.infinity.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiBlockManagers {

    public static final MultiBlockManagerBeta<Generators> POWER_MULTIBLOCKS = new MultiBlockManagerBeta<>();

    static {

        POWER_MULTIBLOCKS.register("infinity:furnace_generator",
                Generators.FURNACE,
                BlockPatternBuilder.start()
                        .aisle("W0W", "0*0", "W0W")
                        .where('0', a -> a.getState().is(Blocks.FURNACE) && a.getState().getValue(BlockStateProperties.LIT))
                        .where('W', a -> a.getState().is(BlockTags.WALLS))
                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                        .build()
        );

        POWER_MULTIBLOCKS.register("infinity:lava_generator",
                Generators.LAVA,
                BlockPatternBuilder.start()
                        .aisle("LLL", "L*L", "LLL")
                        .where('L', a -> a.getState().is(Blocks.LAVA))
                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                        .build()
        );

        POWER_MULTIBLOCKS.register("infinity:gold_generator",
                Generators.GOLD,
                BlockPatternBuilder.start()
                        .aisle("         ", "    G    ", "         ")
                        .aisle("    G    ", "   GGG   ", "    G    ")
                        .aisle("         ", "    G    ", "         ")
                        .aisle("         ", " G     G ", "         ")
                        .aisle(" G     G ", "GGG * GGG", " G     G ")
                        .aisle("         ", " G     G ", "         ")
                        .aisle("         ", "    G    ", "         ")
                        .aisle("    G    ", "   GGG   ", "    G    ")
                        .aisle("         ", "    G    ", "         ")
                        .where('G', a -> a.getState().is(Blocks.GOLD_BLOCK))
                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                        .build()
        );


        POWER_MULTIBLOCKS.register("infinity:end_bricks",
                Generators.END_BRICKS,
                BlockPatternBuilder.start()
                        .aisle("       ", "       ","       ","   R   ","       ","       ","       ")
                        .aisle("R     R", "       ","       ","   *   ","       ","       ","R     R")
                        .aisle("EEEEEEE", "EEEEEEE","EEEEEEE","EEEEEEE","EEEEEEE","EEEEEEE","EEEEEEE")
                        .where('0', a -> a.getState().is(Blocks.FURNACE))// && a.getState().getValue(BlockStateProperties.LIT))
                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                        .where('E', a -> a.getState().is(Blocks.END_STONE_BRICKS))
                        .where('R', a -> a.getState().is(Blocks.END_ROD))
                        .build()
        );


        POWER_MULTIBLOCKS.register(
                "infinity:tier_1",
                Generators.TIER_1,
                BlockPatternBuilder.start()
                    //    .aisle("C   C", "     ", "     ", "     ", "C   C")
                        .aisle("0   0", "     ", "  *  ", "     ", "0   0")
                        .aisle("00000", "0   0", "0   0", "0   0", "00000")
                    //    .aisle("0   0", "     ", "     ", "     ", "0   0")
                 //       .aisle("0   0", "     ", "     ", "     ", "0   0")
                 //       .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .where('0', a -> a.getState().is(Blocks.IRON_BLOCK))
                //        .where('C', a -> a.getState().is(com.benbenlaw.opolisutilities.block.ModBlocks.ENDER_SCRAMBLER.get()))

                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
             //           .where(' ', a -> a.getState().is(Blocks.AIR))
                        .build()
        );

        if (ModList.get().isLoaded("bloodmagic")) {
            Block rune = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("bloodmagic:blankrune"));

            POWER_MULTIBLOCKS.register("infinity:blood_magic_generator",
                    Generators.BLOOD_MAGIC_ALTAR,
                    BlockPatternBuilder.start()
                            .aisle("R     R", "       ", "       ", "       ", "       ", "       ", "R     R")
                            .aisle("B     B", "       ", "       ", "   *   ", "       ", "       ", "B     B")
                            .aisle("B     B", "       ", "  UUU  ", "  U U  ", "  UUU  ", "       ", "B     B")
                            .aisle(" UUUUU ", "U     U", "U     U", "U     U", "U     U", "U     U", " UUUUU ")
                            .where('U', a ->  a.getState().is(rune))
                            .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                            .where('R', a -> a.getState().is(Blocks.REDSTONE_BLOCK))
                            .where('B', a -> a.getState().is(BlockTags.STONE_BRICKS))
                            .build()
            );

        }


/*
        POWER_MULTIBLOCKS.register(
                "infinity:tier_2",
                Generators.TIER_1,
                BlockPatternBuilder.start()
                        .aisle("C   C", "     ", "     ", "     ", "C   C")
                        .aisle("0   0", "     ", "  *  ", "     ", "0   0")
                        .aisle("00000", "0   0", "0   0", "0   0", "00000")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .aisle("0   0", "     ", "     ", "     ", "0   0")
                        .where('0', a -> a.getState().is(Blocks.GOLD_BLOCK))
                        .where('C', a -> a.getState().is(com.benbenlaw.opolisutilities.block.ModBlocks.ENDER_SCRAMBLER.get()))
                        .where('*', a -> a.getState().is(ModBlocks.INFINITY_GENERATOR.get()))
                        .where(' ', a -> a.getState().is(Blocks.AIR))
                        .build()
        );

 */


    }

}
