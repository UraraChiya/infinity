package com.benbenlaw.infinity.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

import java.util.function.Supplier;

public class MultiBlockChecker {
    private BlockPattern pattern;
    private BlockPattern.BlockPatternMatch match;

    public BlockPattern.BlockPatternMatch isValid(Level level, BlockPos pos, Supplier<BlockPattern> patternSupplier) {
        if (pattern == null) {
            this.pattern = patternSupplier.get();
        }
        var result = pattern.find(level, pos);
        if (result != null) {
            this.match = result;
            return result;
        }
        this.pattern = null;
        this.match = null;
        return null;
    }

    public BlockPattern.BlockPatternMatch getMatch() {
        return match;
    }

}
