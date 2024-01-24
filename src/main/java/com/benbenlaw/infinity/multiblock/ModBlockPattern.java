package com.benbenlaw.infinity.multiblock;


import org.mangorage.mangomultiblock.core.IMultiBlockPattern;

public record ModBlockPattern<T>(String ID, T data, IMultiBlockPattern structure) { }
