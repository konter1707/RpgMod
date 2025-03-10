package com.marshmallow.rpgmod.mixins;

import com.marshmallow.rpgmod.PlayerProgressHelper;
import com.marshmallow.rpgmod.items.RpgItems;
import net.minecraft.advancement.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {
    @Shadow
    private ServerPlayerEntity owner;

    @Shadow public abstract AdvancementProgress getProgress(AdvancementEntry advancement);

    @Inject(method = "grantCriterion", at = @At("TAIL"))
    public void onGrantCriterion(AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        Advancement advancement = advancementEntry.value();
        if (advancement != null && !advancement.isRoot()) {
            if (getProgress(advancementEntry).isDone()) {
                if (advancement.display().isPresent()) {
                    AdvancementFrame frame = advancement.display().get().getFrame();
                    int xp = switch (frame) {
                        case TASK -> 10;
                        case CHALLENGE -> 30;
                        case GOAL -> 50;
                    };
                    PlayerProgressHelper.setCustomPlayerEarnedXp(100);
                }
            }
        } else {
            if (advancementEntry.id().getPath().equals("bacap/root")) {
                PlayerInventory inventory = owner.getInventory();
                ItemStack bookStack = new ItemStack(RpgItems.RPG_BOOK_ITEM);
                if (!inventory.contains(bookStack)) {
                    inventory.insertStack(bookStack);
                }
            }
        }
    }
}
