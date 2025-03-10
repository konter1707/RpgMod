package com.marshmallow.rpgmod.mixins;

import com.marshmallow.rpgmod.PlayerTemperatureProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract boolean isPlayer();

    @Inject(method = "isFrozen", at = @At("HEAD"), cancellable = true)
    private void isFrozen(CallbackInfoReturnable<Boolean> cir) {
        if(this.isPlayer()) {
            InGameHud inGameHud = MinecraftClient.getInstance().inGameHud;
            if (inGameHud instanceof PlayerTemperatureProvider provider) {
                cir.setReturnValue(provider.getPlayerTemperature() == 140);
            }
        }
    }
}
