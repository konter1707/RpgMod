package com.marshmallow.rpgmod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void blockPlayerInventorySlots(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (!player.getAbilities().creativeMode) {
            ScreenHandler screenHandler = (ScreenHandler) (Object) this;
            if (slotIndex >= 0 && slotIndex < screenHandler.slots.size()) {
                boolean isPlayerScreenHandler = screenHandler instanceof PlayerScreenHandler;
                int playerInventoryStart = screenHandler.slots.size() - (isPlayerScreenHandler ? 37 : 36);
                int playerInventoryEnd = screenHandler.slots.size() - (isPlayerScreenHandler ? 10 : 9);
                if (slotIndex >= playerInventoryStart && slotIndex < playerInventoryEnd) {
                    ci.cancel();
                }
            }
        }
    }

    @ModifyVariable(method = "insertItem", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int modifyStartIndex(int startIndex) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && !player.getAbilities().creativeMode) {
            if (startIndex > 0) {
                return startIndex + 27;
            }
        }
        return startIndex;
    }
}
