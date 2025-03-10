package com.marshmallow.rpgmod;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class PlayerProgressHelper {
    private static final String RPG_MOD = "rpg_mod";
    private static final String EARNED_XP_KEY = "rpgmod_earned_xp";
    private static final String LEVEL_KEY = "rpgmod_level";
    private static final String SKILL_KEY = "rpgmod_skill";
    private static final String TOTAL_XP_KEY = "rpgmod_total_xp";

    public static final String HEALTH_LEVEL_KEY = "rpgmod_health_level";
    public static final String FOOD_FULL_LEVEL_KEY = "rpgmod_food_fool_level";
    public static final String ENDURANCE_LEVEL_KEY = "rpgmod_endurance_level";

    public static final String HEALTH_KEY = "rpgmod_health";
    public static final String STRENGTH_KEY = "rpgmod_strength";
    public static final String PROTECTION_KEY = "rpgmod_protection";
    public static final String SATIETY_KEY = "rpgmod_satiety";
    public static final String SPEED_KEY = "rpgmod_speed";
    public static final String CRAFTING_KEY = "rpgmod_crafting";

    private static int rpgmod_level;
    private static int rpgmod_skill;
    private static int rpgmod_total_xp;
    private static int rpgmod_earned_xp;

    private static int rpgmod_health_level;
    private static int rpgmod_food_full_level;
    private static int rpgmod_endurance_level;

    public static int getSkill() {
        return rpgmod_skill;
    }

    public static void setSkill(int value) {
        rpgmod_skill = value;
    }

    public static int getLevel() {
        return rpgmod_level;
    }

    public static void setLevel(int value) {
        rpgmod_level = value;
    }

    public static int getTotalXp() {
        return rpgmod_total_xp;
    }

    public static void setTotalXp(int value) {
        rpgmod_total_xp = value;
    }

    public static int getEarnedXp() {
        return rpgmod_earned_xp;
    }

    public static void setEarnedXp(int value) {
        rpgmod_earned_xp = value;
    }

    public static int getHealthLevel() {
        return rpgmod_health_level;
    }

    public static int getFoodFullLevel() {
        return rpgmod_food_full_level;
    }

    public static int getEnduranceLevel() {
        return rpgmod_endurance_level;
    }

    public static void updateSkillLevel(int index) {
        switch (index) {
            case 0 -> rpgmod_health_level++;
            case 1 -> rpgmod_food_full_level++;
            case 2 -> rpgmod_endurance_level++;
            default -> RpgMod.LOGGER.debug("");
        };

    }

    public static int getProgressBarHeight() {
        if (rpgmod_earned_xp == 0 || rpgmod_total_xp == 0) {
            return 0;
        }
        double percent = (double) rpgmod_earned_xp / rpgmod_total_xp;
        return (int) (182 * percent);
    }

    public static void writeNbt(NbtCompound nbt) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt(EARNED_XP_KEY, rpgmod_earned_xp);
        nbtCompound.putInt(TOTAL_XP_KEY, rpgmod_total_xp);
        nbtCompound.putInt(LEVEL_KEY, rpgmod_level);
        nbtCompound.putInt(SKILL_KEY, rpgmod_skill);
        nbtCompound.putInt(HEALTH_LEVEL_KEY, rpgmod_health_level);
        nbtCompound.putInt(FOOD_FULL_LEVEL_KEY, rpgmod_food_full_level);
        nbt.put(RPG_MOD, nbtCompound);
    }

    public static void readNbt(NbtCompound nbt) {
        rpgmod_level = 0;
        rpgmod_skill = 0;
        rpgmod_earned_xp = 0;
        rpgmod_health_level = 0;
        rpgmod_food_full_level = 0;
        rpgmod_endurance_level = 0;
        rpgmod_total_xp = 100;
        if (nbt.contains("abilities", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound(RPG_MOD);
            rpgmod_earned_xp = getIntOrDefault(nbtCompound, EARNED_XP_KEY);
            rpgmod_total_xp = getIntOrDefault(nbtCompound, TOTAL_XP_KEY);
            rpgmod_level = getIntOrDefault(nbtCompound, LEVEL_KEY);
            rpgmod_skill = getIntOrDefault(nbtCompound, SKILL_KEY);
            rpgmod_health_level = getIntOrDefault(nbtCompound, HEALTH_LEVEL_KEY);
            rpgmod_food_full_level = getIntOrDefault(nbtCompound, FOOD_FULL_LEVEL_KEY);
        }
    }

    public static void setCustomPlayerEarnedXp(int value) {
        rpgmod_earned_xp += value;
        if (rpgmod_earned_xp >= rpgmod_total_xp) {
            rpgmod_earned_xp = rpgmod_earned_xp - rpgmod_total_xp;
            rpgmod_total_xp = rpgmod_total_xp * 2;
            rpgmod_level++;
            rpgmod_skill++;
        }
    }

    private static int getIntOrDefault(NbtCompound nbtCompound, String key) {
        return nbtCompound.contains(key) ? nbtCompound.getInt(key) : key.equals(TOTAL_XP_KEY) ? 100 : 0;
    }
}
