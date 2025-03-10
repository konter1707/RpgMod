package com.marshmallow.rpgmod.screen;

import com.marshmallow.rpgmod.BuildConfig;
import com.marshmallow.rpgmod.PlayerProgressHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PlayerSkinWidget;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CustomScreen extends Screen {
    private static final int SKIN_WIDTH = 80;
    private static final int SKIN_HEIGHT = 60;
    private static final Identifier BACKGROUND_LARGE = new Identifier(BuildConfig.MOD_ID, "textures/gui/background_large.png");
    private static final Identifier EXPERIENCE_BAR_BACKGROUND_TEXTURE = new Identifier("minecraft:hud/experience_bar_background");
    private static final Identifier EXPERIENCE_BAR_PROGRESS_TEXTURE = new Identifier("minecraft:hud/experience_bar_progress");
    private static final Identifier HEAT_HARDCORE_FULL = new Identifier("minecraft:hud/heart/hardcore_full");
    private static final Identifier ARMOR_FULL = new Identifier("minecraft:hud/armor_full");
    private static final Identifier FOOD_FULL = new Identifier("minecraft:hud/food_full");
    private static final Identifier RPG_SPEED = new Identifier(BuildConfig.MOD_ID, "textures/gui/rpg_speed.png");
    private static final Identifier RPG_STRENGTH = new Identifier(BuildConfig.MOD_ID, "textures/gui/rpg_strength.png");
    private static final Identifier RPG_PLUS = new Identifier(BuildConfig.MOD_ID, "textures/gui/rpg_plus.png");
    private static final Identifier FOOTER_SEPARATOR = new Identifier("minecraft", "textures/gui/footer_separator.png");

    private PlayerSkinWidget playerSkinWidget;
    int heightBackground, widthBackground, x, y;
    int fontHeight, textWithFood, textWidthSpeed;
    List<ButtonWidget> pointButtons = new ArrayList<>();
    String[] labels = {getStatusTextLevel(0), getStatusTextLevel(1), getStatusTextLevel(2)};
    Identifier[] icons = {HEAT_HARDCORE_FULL, FOOD_FULL, RPG_SPEED, RPG_STRENGTH, ARMOR_FULL, FOOD_FULL};
    int[] yOffsets = new int[]{};
    int windowWidth;
    int windowHeight;

    public CustomScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        fontHeight = textRenderer.fontHeight;
        windowWidth = client.getWindow().getScaledWidth();
        windowHeight = client.getWindow().getScaledHeight();
        heightBackground = windowHeight - 40;
        widthBackground = windowWidth - 40;
        x = (windowWidth - widthBackground) / 2;
        y = (windowHeight - heightBackground) / 2;

        textWidthSpeed = textRenderer.getWidth(getStatusTextLevel(2));
        textWithFood = textRenderer.getWidth("Сопротивление к ядам");
        int basePointYOffset = y + 78 + SKIN_HEIGHT;
        yOffsets = new int[]{
                basePointYOffset + fontHeight,
                basePointYOffset + (fontHeight * 2) + 10,
                basePointYOffset + (fontHeight * 3) + 20,
        };
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            int textW = textRenderer.getWidth(labels[2]);
            ButtonWidget button = ButtonWidget
                    .builder(Text.empty(), btn -> {
                        int skill = PlayerProgressHelper.getSkill();
                        skill--;
                        PlayerProgressHelper.setSkill(skill);
                        PlayerProgressHelper.updateSkillLevel(finalI);
                        labels[finalI] = getStatusTextLevel(finalI);
                        btn.active = false;
                    })
                    .dimensions(((width / 2) + (textW / 2)) + 10, yOffsets[i], 13, 13)
                    .build();
            pointButtons.add(addDrawableChild(button));
        }
        playerSkinWidget = addDrawableChild(new PlayerSkinWidget(SKIN_WIDTH, SKIN_HEIGHT, client.getEntityModelLoader(), texturesSupplier(client.getGameProfile())));
        playerSkinWidget.setPosition(x + 40, y + 12 + fontHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTexture(BACKGROUND_LARGE, x, y, 0, 0, widthBackground, heightBackground, widthBackground, heightBackground);
        int textWidth = textRenderer.getWidth("Skill Player");
        drawText(context, "Skill Player", (windowWidth - textWidth) / 2, y + 6);
        playerSkinWidget.render(context, 0, 0, 0);

        int marginX = x + 50 + SKIN_WIDTH;

        int textWidthHeat = textRenderer.getWidth("Здоровье: 6.0");
        int textWidthEndurance = textRenderer.getWidth("Выносливость: 1.0");
        int textWidthMaxFood = textRenderer.getWidth("Сытость: 10.0");
        context.drawGuiTexture(HEAT_HARDCORE_FULL, marginX, y + 24 + fontHeight, fontHeight, fontHeight);
        drawText(context, "Здоровье: 6.0", marginX + fontHeight + 6, y + 24 + fontHeight);
        context.drawTexture(RPG_SPEED, marginX, y + 24 + (fontHeight * 2) + 6, 0, 0, fontHeight, fontHeight, fontHeight, fontHeight);
        drawText(context, "Выносливость: 1.0", marginX + fontHeight + 6, y + 24 + (fontHeight * 2) + 6);

        context.drawGuiTexture(FOOD_FULL, marginX + textWidthHeat + 60, y + 24 + fontHeight, fontHeight, fontHeight);
        drawText(context, "Сытость: 10.0", marginX + fontHeight + 6 + textWidthHeat + 60, y + 24 + fontHeight);
        context.drawGuiTexture(ARMOR_FULL, marginX + textWidthHeat + 60, y + 24 + (fontHeight * 2) + 6, fontHeight, fontHeight);
        drawText(context, "Защита: 1.0", marginX + fontHeight + 6 + textWidthHeat + 60, y + 24 + (textRenderer.fontHeight * 2) + 6);

        context.drawTexture(FOOTER_SEPARATOR, x, y + 20 + textRenderer.fontHeight + SKIN_HEIGHT, 0, 0, width - 40, 2);

        String xpText = "XP " + PlayerProgressHelper.getEarnedXp() + " / " + PlayerProgressHelper.getTotalXp();
        String levelText = "Уровень " + PlayerProgressHelper.getLevel();
        String skillText = "Очки навыков " + PlayerProgressHelper.getSkill();
        int textWidthPoint = textRenderer.getWidth(xpText);
        int textWidthLevel = textRenderer.getWidth(skillText);
        drawText(context, levelText, marginX, y + 30 + textRenderer.fontHeight + SKIN_HEIGHT);
        drawText(context, skillText, marginX + (182 - textWidthLevel), y + 30 + textRenderer.fontHeight + SKIN_HEIGHT);
        context.drawGuiTexture(EXPERIENCE_BAR_BACKGROUND_TEXTURE, (width - 182) / 2, y + 45 + textRenderer.fontHeight + SKIN_HEIGHT, 182, 5);
        context.drawGuiTexture(EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, (width - 182) / 2, y + 45 + textRenderer.fontHeight + SKIN_HEIGHT, PlayerProgressHelper.getProgressBarHeight(), 5);
        drawText(context, xpText, (width - textWidthPoint) / 2, y + 60 + textRenderer.fontHeight + SKIN_HEIGHT);

        for (int i = 0; i < 3; i++) {
            int yOffset = yOffsets[i];
            int textW = textRenderer.getWidth(labels[2]);
            int iconX = ((width - textW - 45) / 2);
            if (i == 2) {
                context.drawTexture(icons[i], iconX, yOffset, 0, 0, 13, 13, 13, 13);
            } else {
                context.drawGuiTexture(icons[i], iconX, yOffset, 13, 13);
            }
            drawText(context, labels[i], (width - textW) / 2, yOffset + 4);
            ButtonWidget buttonWidget = pointButtons.get(i);
            buttonWidget.active = PlayerProgressHelper.getSkill() > 0;
            buttonWidget.render(context, mouseX, mouseY, delta);
            if (buttonWidget.isHovered()) {
                context.drawTooltip(this.textRenderer, Text.of("Описание кнопки"), mouseX, mouseY);
                int pointX = marginX + fontHeight + 10;
                int pointY = y + 24 + fontHeight;
                if (i == 0) {
                    pointX += textWidthHeat;
                } else if (i == 1) {
                    pointX += textWidthEndurance;
                    pointY = y + 24 + (fontHeight * 2) + 6;
                } else {
                    pointX += textWidthHeat + 60 + textWidthMaxFood;
                }
                context.drawText(textRenderer, "+1", pointX, pointY, 0x42A317, false);
            }
            context.drawTexture(RPG_PLUS, ((width / 2) + (textW / 2)) + 13, yOffset + 3, 0, 0, 7, 7, 7, 7);
        }
    }

    private String getStatusTextLevel(int index) {
        return switch (index) {
            case 0 -> String.format("Здоровье %d / 20", PlayerProgressHelper.getHealthLevel());
            case 1 -> String.format("Сытость %d / 20", PlayerProgressHelper.getFoodFullLevel());
            case 2 -> String.format("Выносливость %d / 20", PlayerProgressHelper.getEnduranceLevel());
            default -> "Такого нету";
        };
    }

    private void drawText(DrawContext context, String text, int x, int y) {
        context.drawText(textRenderer, text, x, y, 0x404040, false);
    }

    private static java.util.function.Supplier<SkinTextures> texturesSupplier(GameProfile profile) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        PlayerSkinProvider playerSkinProvider = minecraftClient.getSkinProvider();
        CompletableFuture<SkinTextures> completableFuture = playerSkinProvider.fetchSkinTextures(profile);
        boolean bl = !minecraftClient.uuidEquals(profile.getId());
        SkinTextures skinTextures = DefaultSkinHelper.getSkinTextures(profile);
        return () -> {
            SkinTextures skinTextures2 = completableFuture.getNow(skinTextures);
            if (bl && !skinTextures2.secure()) {
                return skinTextures;
            }
            return skinTextures2;
        };
    }
}
