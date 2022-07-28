package net.Mystery2099.mystmultitoolmod.client;


import net.Mystery2099.mystmultitoolmod.MystMultiToolMod;
import net.Mystery2099.mystmultitoolmod.config.MystMultiToolModClientConfig;
import net.Mystery2099.mystmultitoolmod.config.keys.KeyBinds;
import net.Mystery2099.mystmultitoolmod.item.ModItems;
import net.Mystery2099.mystmultitoolmod.item.custom.MultiToolItem;
import net.Mystery2099.mystmultitoolmod.util.enums.DefaultToolModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = MystMultiToolMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class MystMultiToolModClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            KeyBinds.registerKeys();
            for (var tool : ModItems.getItemList()) registerOverrides(tool.get());
        });
    }

    private static void registerOverrides(MultiToolItem tool) {
        ItemProperties.register(tool, new ResourceLocation(MystMultiToolMod.MOD_ID, "digging"), (pStack, pLevel, pEntity, pSeed) -> (MultiToolItem.checkStackToolMode(pStack, "Shovel") && MystMultiToolModClientConfig.DYNAMIC_TOOL_MODEL.get()) ||
                (MystMultiToolModClientConfig.DEFAULT_TOOL_MODEL.get() == DefaultToolModel.SHOVEL &&
                        MultiToolItem.checkStackToolMode(pStack, "Default")) ? 1 : 0);

        ItemProperties.register(tool, new ResourceLocation(MystMultiToolMod.MOD_ID, "mining"), (pStack, pLevel, pEntity, pSeed) -> (MultiToolItem.checkStackToolMode(pStack, "Pickaxe") && MystMultiToolModClientConfig.DYNAMIC_TOOL_MODEL.get()) ||
                (MystMultiToolModClientConfig.DEFAULT_TOOL_MODEL.get() == DefaultToolModel.PICKAXE &&
                        MultiToolItem.checkStackToolMode(pStack, "Default")) ? 1 : 0);

        ItemProperties.register(tool, new ResourceLocation(MystMultiToolMod.MOD_ID, "chopping"), (pStack, pLevel, pEntity, pSeed) -> (MultiToolItem.checkStackToolMode(pStack, "Axe") && MystMultiToolModClientConfig.DYNAMIC_TOOL_MODEL.get()) ||
                (MystMultiToolModClientConfig.DEFAULT_TOOL_MODEL.get() == DefaultToolModel.AXE &&
                        MultiToolItem.checkStackToolMode(pStack, "Default")) ? 1 : 0);

        ItemProperties.register(tool, new ResourceLocation(MystMultiToolMod.MOD_ID, "hoeing"), (pStack, pLevel, pEntity, pSeed) -> (MultiToolItem.checkStackToolMode(pStack, "Hoe") && MystMultiToolModClientConfig.DYNAMIC_TOOL_MODEL.get()) ||
                (MystMultiToolModClientConfig.DEFAULT_TOOL_MODEL.get() == DefaultToolModel.HOE &&
                        MultiToolItem.checkStackToolMode(pStack, "Default")) ? 1 : 0);
    }
}
