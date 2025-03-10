package com.marshmallow.rpgmod.screen;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;

public class EntityHudOverlay implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            Entity target = findEntityPlayerIsLookingAt(client.player);
            if (target instanceof LivingEntity livingEntity) {
                EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
                EntityRenderer<? super LivingEntity> render = dispatcher.getRenderer(livingEntity);
                if (render != null) {
                    client.getTextureManager().bindTexture(getEntityTexture(livingEntity));
                }

                Text name = livingEntity.getName();
                float maxHealth = livingEntity.getMaxHealth();
                float health = livingEntity.getHealth();
                drawContext.drawText(client.textRenderer, name, 10, 10,  0xFFFFFF, false);
                drawContext.drawText(client.textRenderer, "Здоровье: " + health + " / " + maxHealth, 10, 10 + client.textRenderer.fontHeight + 5, 0xFFFFFF, false);
            }
        }
    }

    private Entity findEntityPlayerIsLookingAt(ClientPlayerEntity player) {
        Vec3d eyePosition = player.getCameraPosVec(1.0f);
        Vec3d lookVector = player.getRotationVector();
        Vec3d end = eyePosition.add(lookVector.multiply(40.0));
        List<Entity> entities = player.getWorld().getEntitiesByClass(
                Entity.class,
                player.getBoundingBox().expand(10),
                entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity != player
        );
        for (Entity entity : entities) {
            Box box = entity.getBoundingBox().expand(0.1);
            Optional<Vec3d> hit = box.raycast(eyePosition, end);
            if (hit.isPresent()) {
                return entity;
            }
        }
        return null;
    }

    private Identifier getEntityTexture(Entity entity) {
        EntityRenderer<? super Entity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (renderer != null) {
            return renderer.getTexture(entity);
        }
        return null;
    }

}
