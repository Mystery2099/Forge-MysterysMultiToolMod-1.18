package net.Mystery2099.mystmultitoolmod.util.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.Mystery2099.mystmultitoolmod.MystMultiToolMod;

public class ModTags {

    public static class ModItemTags {
        public static final TagKey<Item> MULTITOOLS = ItemTags.create(new ResourceLocation(MystMultiToolMod.MOD_ID, "multitools"));
    }
    public static class ModBlockTags {
        public static final TagKey<Block> MINEABLE_WITH_MULTITOOL = BlockTags.create(new ResourceLocation(MystMultiToolMod.MOD_ID, "mineable/multitool"));
    }

}
