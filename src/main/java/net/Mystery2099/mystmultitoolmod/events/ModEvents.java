package net.Mystery2099.mystmultitoolmod.events;


import net.Mystery2099.mystmultitoolmod.MystMultiToolMod;
import net.Mystery2099.mystmultitoolmod.config.keys.KeyBinds;
import net.Mystery2099.mystmultitoolmod.item.ModItems;
import net.Mystery2099.mystmultitoolmod.item.custom.MultiToolItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.Mystery2099.mystmultitoolmod.MystMultiToolMod.MOD_ID;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().player != null) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                String toolMode = MOD_ID+".toolMode";
                ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (KeyBinds.RESET_KEY.consumeClick() && checkItemInHand(stackInHand)) {
                    MultiToolItem.addToolModeNbt(stackInHand, "Default");
                } else if (KeyBinds.NEXT_MODE_KEY.consumeClick() && checkItemInHand(stackInHand)) {
                    MultiToolItem.addToolModeNbt(stackInHand, stackInHand.getTag().getString(toolMode).contains("Default") || !stackInHand.hasTag() ? "Shovel" :
                            stackInHand.getTag().getString(toolMode).contains("Shovel") ? "Pickaxe" :
                                    stackInHand.getTag().getString(toolMode).contains("Pickaxe") ? "Axe" :
                                            stackInHand.getTag().getString(toolMode).contains("Axe") ? "Hoe" : "Default");
                } else if (KeyBinds.PREVIOUS_MODE_KEY.consumeClick() && checkItemInHand(stackInHand)) {
                    MultiToolItem.addToolModeNbt(stackInHand, stackInHand.getTag().getString(toolMode).contains("Default") || !stackInHand.hasTag() ? "Hoe" :
                                                stackInHand.getTag().getString(toolMode).contains("Hoe") ? "Axe" ://MultiToolItem itemInHand = getItemInHandAsMultiTool();
                                                        stackInHand.getTag().getString(toolMode).contains("Axe") ? "Pickaxe" :
                                                                stackInHand.getTag().getString(toolMode).contains("Pickaxe") ? "Shovel" : "Default");
                }
            }
        }
        public static boolean checkItemInHand(ItemStack stackInHand) {
            return stackInHand.is(ModItems.WOODEN_MULTI_TOOL.get()) ||
                    stackInHand.is(ModItems.STONE_MULTI_TOOL.get()) ||
                    stackInHand.is(ModItems.GOLDEN_MULTI_TOOL.get()) ||
                    stackInHand.is(ModItems.IRON_MULTI_TOOL.get()) ||
                    stackInHand.is(ModItems.DIAMOND_MULTI_TOOL.get()) ||
                    stackInHand.is(ModItems.NETHERITE_MULTI_TOOL.get());
        }
    }
}
