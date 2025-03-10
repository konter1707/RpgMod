package com.marshmallow.rpgmod.mixins;

import com.marshmallow.rpgmod.PlayerTemperatureProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements PlayerTemperatureProvider {

    @Shadow @Final private static Identifier POWDER_SNOW_OUTLINE;

    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Shadow protected abstract void renderStatusBars(DrawContext context);

    @Unique
    private int playerTemperature = 0;

    @ModifyConstant(method = "renderStatusBars", constant = @Constant(intValue = 10, ordinal = 5))
    private int replaceCount(int ordinalValue) {
        return 4;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            Biome biome = player.getWorld().getBiome(player.getBlockPos()).value();
            if (isNearHeatSource(player)) {
                if (playerTemperature > 0) {
                    playerTemperature = Math.max(0, playerTemperature - 2);
                }
            } else if (biome.getTemperature() <= 0) {
                playerTemperature = Math.min(playerTemperature + 4, 140);
            } else {
                playerTemperature = Math.max(0, playerTemperature - 1);
            }

            if (playerTemperature > 0) {
                if (playerTemperature == 140) {
                    renderStatusBars(context);
                    player.damage(player.getWorld().getDamageSources().generic(), 1f);
                }
                RenderSystem.enableBlend();
                renderOverlay(context, POWDER_SNOW_OUTLINE, (float) playerTemperature / 140);
                RenderSystem.disableBlend();
            }
        }
    }

    @Override
    public int getPlayerTemperature() {
        return playerTemperature;
    }

    private boolean isNearHeatSource(PlayerEntity player) {
        int radius = 4;
        BlockPos pos = player.getBlockPos();
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, -1, -radius), pos.add(radius, 1, radius))) {
            Block block = player.getWorld().getBlockState(blockPos).getBlock();
            if (block == Blocks.TORCH || block == Blocks.CAMPFIRE || block == Blocks.LAVA) {
                return true;
            }
        }
        return false;
    }
}
