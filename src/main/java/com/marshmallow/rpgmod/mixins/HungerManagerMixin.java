package com.marshmallow.rpgmod.mixins;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Shadow
    private int foodLevel = 8;

    @Shadow
    private float saturationLevel = 2;

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void onAdd(int food, float saturationModifier, CallbackInfo ci) {
        foodLevel = Math.min(food + foodLevel, 8);
        saturationLevel = Math.min(saturationLevel + (float)food * saturationModifier * 2.0f, (float)foodLevel);
        ci.cancel();
    }

    @Inject(method = "isNotFull", at = @At("HEAD"), cancellable = true)
    public void isNotFull(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(foodLevel < 8);
        cir.cancel();
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 20, ordinal = 0))
    private int injectCustomValue20(int ordinalValue) {
        return 8;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 18, ordinal = 0))
    private int injectCustomValue18(int ordinalValue) {
        return 8;
    }
}
