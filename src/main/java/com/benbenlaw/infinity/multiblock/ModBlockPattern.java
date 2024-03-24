package com.benbenlaw.infinity.multiblock;


import org.mangorage.mangomultiblock.core.impl.IMultiBlockPattern;

public record ModBlockPattern<T>(String ID, T data, IMultiBlockPattern structure) { }
