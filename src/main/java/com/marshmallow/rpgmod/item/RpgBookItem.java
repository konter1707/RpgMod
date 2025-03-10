package com.marshmallow.rpgmod.item;

import com.marshmallow.rpgmod.screen.CustomScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class RpgBookItem extends Item {
    public RpgBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            client.setScreen(new CustomScreen(Text.literal("Yes")));
        });
//        user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
//                (syncId, inventory, player) -> GenericContainerScreenHandler.createGeneric9x6(syncId, inventory),
//                Text.literal("Player Inventory")
//        ));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }
}
