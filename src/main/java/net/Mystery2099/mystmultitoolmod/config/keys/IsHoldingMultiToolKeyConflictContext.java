package net.Mystery2099.mystmultitoolmod.config.keys;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.Mystery2099.mystmultitoolmod.item.ModItems;

public class IsHoldingMultiToolKeyConflictContext implements IKeyConflictContext {
    @Override
    public boolean isActive() {
        Player player = Minecraft.getInstance().player;
        return (player.isHolding(ModItems.WOODEN_MULTI_TOOL.get()) ||
                player.isHolding(ModItems.STONE_MULTI_TOOL.get()) ||
                player.isHolding(ModItems.GOLDEN_MULTI_TOOL.get()) ||
                player.isHolding(ModItems.IRON_MULTI_TOOL.get()) ||
                player.isHolding(ModItems.DIAMOND_MULTI_TOOL.get()) ||
                player.isHolding(ModItems.NETHERITE_MULTI_TOOL.get())) &&
                KeyConflictContext.IN_GAME.isActive();
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
        return this == other;
    }
    public static final IsHoldingMultiToolKeyConflictContext INSTANCE = new IsHoldingMultiToolKeyConflictContext();
}
