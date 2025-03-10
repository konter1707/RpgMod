package com.marshmallow.rpgmod.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Shadow @Final public DefaultedList<ItemStack> main;

    @Inject(method = "getEmptySlot", at = @At("HEAD"), cancellable = true)
    private void restrictSlots(CallbackInfoReturnable<Integer> cir) {
        if (!player.getAbilities().creativeMode) {
            for (int i = 0; i < 9; i++) {
                if (this.main.get(i).isEmpty()) {
                    cir.setReturnValue(i);
                    return;
                }
            }
            cir.setReturnValue(-1);
        }
    }
}
