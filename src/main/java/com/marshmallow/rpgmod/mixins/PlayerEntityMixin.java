package com.marshmallow.rpgmod.mixins;

import com.marshmallow.rpgmod.PlayerProgressHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void loadCustomDataFromNbt(NbtCompound nbtCompound, CallbackInfo ci) {
        PlayerProgressHelper.writeNbt(nbtCompound);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbtCompound, CallbackInfo ci) {
        PlayerProgressHelper.readNbt(nbtCompound);
    }
}
