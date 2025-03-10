package com.marshmallow.rpgmod.items;

import com.marshmallow.rpgmod.BuildConfig;
import com.marshmallow.rpgmod.item.RpgBookItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RpgItems {

    public static final String RPG_BOOK = "rpg_book";
    public static final Item RPG_BOOK_ITEM = registerItem();

    public static Item registerItem() {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(BuildConfig.MOD_ID, RPG_BOOK),
                new RpgBookItem(new Item.Settings().maxCount(1))
        );
    }

    public static void registerMod() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register(itemGroup -> itemGroup.add(RPG_BOOK_ITEM));
    }
}
