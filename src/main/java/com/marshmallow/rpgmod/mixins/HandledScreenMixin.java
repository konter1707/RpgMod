package com.marshmallow.rpgmod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Unique
    private static final Identifier CROSS_ICON = new Identifier("rpgmod", "textures/gui/rpg_locked.png");

    @Shadow @Final protected ScreenHandler handler;

    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void onDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        ScreenHandler handler = this.handler;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && !player.getAbilities().creativeMode) {
            for (int k = 0; k < this.handler.slots.size(); ++k) {
                Slot currentSlot = handler.slots.get(k);
                if (slot == currentSlot) {
                    boolean isPlayerScreenHandler = handler instanceof PlayerScreenHandler;
                    int playerInventoryStart = handler.slots.size() - (isPlayerScreenHandler ? 37 : 36);
                    int playerInventoryEnd = handler.slots.size() - (isPlayerScreenHandler ? 10 : 9);
                    if (k >= playerInventoryStart && k < playerInventoryEnd) {
                        context.drawTexture(CROSS_ICON, slot.x + 4, slot.y + 2, 0, 0, 10, 12, 10, 12);
                    }
                    break;
                }
            }
        }
    }
}
