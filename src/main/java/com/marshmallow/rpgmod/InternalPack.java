package com.marshmallow.rpgmod;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public enum InternalPack {
    BACAP;

    public @NotNull String getDatapackName() {
        return name().toLowerCase();
    }}
