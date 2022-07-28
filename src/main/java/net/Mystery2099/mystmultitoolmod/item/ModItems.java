package net.Mystery2099.mystmultitoolmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import net.Mystery2099.mystmultitoolmod.MystMultiToolMod;
import net.Mystery2099.mystmultitoolmod.item.custom.MultiToolItem;

import java.util.ArrayList;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MystMultiToolMod.MOD_ID);

    public static final RegistryObject<MultiToolItem> WOODEN_MULTI_TOOL = createMultiTool(Tiers.WOOD);
    public static final RegistryObject<MultiToolItem> STONE_MULTI_TOOL = createMultiTool(Tiers.STONE);
    public static final RegistryObject<MultiToolItem> GOLDEN_MULTI_TOOL = createMultiTool(Tiers.GOLD);
    public static final RegistryObject<MultiToolItem> IRON_MULTI_TOOL = createMultiTool(Tiers.IRON);
    public static final RegistryObject<MultiToolItem> DIAMOND_MULTI_TOOL = createMultiTool(Tiers.DIAMOND);
    public static final RegistryObject<MultiToolItem> NETHERITE_MULTI_TOOL = createMultiTool(Tiers.NETHERITE);


    public static ArrayList<RegistryObject<MultiToolItem>> getItemList() {
        ArrayList<RegistryObject<MultiToolItem>> list = new ArrayList<>();
        list.add(WOODEN_MULTI_TOOL);
        list.add(STONE_MULTI_TOOL);
        list.add(GOLDEN_MULTI_TOOL);
        list.add(IRON_MULTI_TOOL);
        list.add(DIAMOND_MULTI_TOOL);
        list.add(NETHERITE_MULTI_TOOL);
        return list;
    }

    private static RegistryObject<MultiToolItem> createMultiTool(Tier tier) {
        String name = tier.toString().toLowerCase();
        name = name.contains("wood") || name.contains("gold") ? name+"en" : name;
        String finalName = name;
        return ITEMS.register(name+"_multi_tool",
                () -> new MultiToolItem(5.0f, -3.0f, tier,
                        finalName != "netherite" ? new Item.Properties().tab(CreativeModeTab.TAB_TOOLS) :
                                new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).fireResistant()));
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
