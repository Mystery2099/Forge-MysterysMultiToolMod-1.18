package net.Mystery2099.mystmultitoolmod.item.custom;

import com.mojang.datafixers.util.Pair;
import net.Mystery2099.mystmultitoolmod.config.MystMultiToolModClientConfig;
import net.Mystery2099.mystmultitoolmod.util.enums.ToolControls;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.Mystery2099.mystmultitoolmod.MystMultiToolMod.MOD_ID;

public class MultiToolItem extends MultiToolAbstractItem {
    public MultiToolItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, pProperties);
        MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
    }
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack thisStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        addConfigInfoNbt(thisStack);
        if (thisStack.hasTag() && thisStack.getTag().getString(MOD_ID+".modelChangesWhen").contains("RIGHT_CLICKS")) {
            String mode;
            mode = blockState.is(BlockTags.MINEABLE_WITH_AXE) && checkStackControls(thisStack, "stripping") ? "Axe" :
                    blockState.is(BlockTags.MINEABLE_WITH_SHOVEL) && checkStackControls(thisStack, "tilling") ? "Hoe" :
                            blockState.is(BlockTags.MINEABLE_WITH_SHOVEL) && checkStackControls(thisStack, "flattening") ? "Shovel" : "Default";
            addToolModeNbt(thisStack, mode);
        }
        //Axe functionality
        if (this.checkStackToolMode(thisStack, "Axe")) {
            Optional<BlockState> optional = Optional.ofNullable(blockState.getToolModifiedState(pContext, ToolActions.AXE_STRIP, false));
            Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockState.getToolModifiedState(pContext, ToolActions.AXE_SCRAPE, false));
            Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockState.getToolModifiedState(pContext, ToolActions.AXE_WAX_OFF, false));
            ItemStack itemstack = pContext.getItemInHand();
            Optional<BlockState> optional3 = Optional.empty();
            if (optional.isPresent()) {
                level.playSound(player, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                optional3 = optional;
            }
            else if (optional1.isPresent()) {
                level.playSound(player, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, blockPos, 0);
                optional3 = optional1;
            }
            else if (optional2.isPresent()) {
                level.playSound(player, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3004, blockPos, 0);
                optional3 = optional2;
            }

            if (optional3.isPresent()) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockPos, itemstack);
                }

                level.setBlock(blockPos, optional3.get(), 11);
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(pContext.getHand()));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        //Hoe Functionality
        if (this.checkStackToolMode(thisStack, "Hoe")) {
            BlockState toolModifiedState = level.getBlockState(blockPos).getToolModifiedState(pContext, ToolActions.HOE_TILL, false);
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
            if (pair == null) {
                return InteractionResult.PASS;
            } else {
                Predicate<UseOnContext> predicate = pair.getFirst();
                Consumer<UseOnContext> consumer = pair.getSecond();
                if (predicate.test(pContext)) {
                    level.playSound(player, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!level.isClientSide) {
                        consumer.accept(pContext);
                        if (player != null) pContext.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(pContext.getHand()));
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        //Shovel Functionality
        if (this.checkStackToolMode(thisStack, "Shovel")) {
            if (pContext.getClickedFace() == Direction.DOWN) return InteractionResult.PASS;
            else {
                BlockState blockstate1 = blockState.getToolModifiedState(pContext, ToolActions.SHOVEL_FLATTEN, false);
                BlockState blockstate2 = null;
                if (blockstate1 != null && level.isEmptyBlock(blockPos.above())) {
                    level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    blockstate2 = blockstate1;
                }
                else if (blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                    if (!level.isClientSide()) level.levelEvent(null, 1009, blockPos, 0);
                    CampfireBlock.dowse(pContext.getPlayer(), level, blockPos, blockState);
                    blockstate2 = blockState.setValue(CampfireBlock.LIT, Boolean.FALSE);
                }

                if (blockstate2 != null) {
                    if (!level.isClientSide) {
                        level.setBlock(blockPos, blockstate2, 11);
                        if (player != null)
                            pContext.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(pContext.getHand()));
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    private void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getPlayer().level.getBlockState(blockPos);
        ItemStack stackInHand = event.getItemStack();
        if (!event.getWorld().isClientSide && event.getUseItem() != Event.Result.DENY &&
                !event.getItemStack().isEmpty() && event.getItemStack().getItem() == this &&
                (stackInHand.getTag().getString(MOD_ID+".modelChangesWhen").contains("PLAYER_HITS"))) {
            if (blockState.is(BlockTags.MINEABLE_WITH_SHOVEL)) addToolModeNbt(stackInHand, "Shovel");
            else if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) addToolModeNbt(stackInHand, "Pickaxe");
            else if (blockState.is(BlockTags.MINEABLE_WITH_AXE)) addToolModeNbt(stackInHand, "Axe");
            else if (blockState.is(BlockTags.MINEABLE_WITH_HOE)) addToolModeNbt(stackInHand, "Hoe");
            else addToolModeNbt(stackInHand, "Default");
        }
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, (p) -> {
            p.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        addToolModeNbt(pStack, "Default");
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            if (!Screen.hasShiftDown()) {
                if (pStack.getTag().contains("mystmultitoolmod.toolMode"))
                    pTooltipComponents.add(new TextComponent(pStack.getTag().getString("mystmultitoolmod.toolMode")));
                pTooltipComponents.add(new TextComponent("Hold SHIFT to View Controls!"));
            }

            if (Screen.hasShiftDown()) {
                if (pStack.getTag().contains("mystmultitoolmod.stripping"))
                    pTooltipComponents.add(new TextComponent(pStack.getTag().getString("mystmultitoolmod.stripping")));
                if (pStack.getTag().contains("mystmultitoolmod.tilling"))
                    pTooltipComponents.add(new TextComponent(pStack.getTag().getString("mystmultitoolmod.tilling")));
                if (pStack.getTag().contains("mystmultitoolmod.flattening"))
                    pTooltipComponents.add(new TextComponent(pStack.getTag().getString("mystmultitoolmod.flattening")));
            }
        }
    }
    public static void addToolModeNbt(ItemStack itemInHand, String mode) {
        itemInHand.getOrCreateTag().putString("mystmultitoolmod.toolMode", "Mode: "+mode);
    }
    private static void addConfigInfoNbt(ItemStack itemInHand) {
        itemInHand.getOrCreateTag().putString("mystmultitoolmod.stripping", "Stripping: "+ MystMultiToolModClientConfig.STRIPPING.get().toString());
        itemInHand.getOrCreateTag().putString("mystmultitoolmod.tilling", "Tilling: "+ MystMultiToolModClientConfig.TILLING.get().toString());
        itemInHand.getOrCreateTag().putString("mystmultitoolmod.flattening", "Flattening: "+ MystMultiToolModClientConfig.FLATTENING.get().toString());
        itemInHand.getOrCreateTag().putString(MOD_ID+".modelChangesWhen", "Model Changes " + MystMultiToolModClientConfig.CHANGE_TOOL_MODEL.get().toString());
    }
    public static boolean checkStackToolMode(ItemStack stack, String mode) {
        return stack.hasTag() && stack.getTag().getString("mystmultitoolmod.toolMode").contains(mode);
    }
    public static boolean checkStackControls(ItemStack stack, String action) {
        String tagString = stack.getTag().getString(MOD_ID+"."+action);
        return stack.hasTag() && (tagString.contains(ToolControls.RIGHT_CLICK_AND_PRESS_SHIFT.toString()) ? Screen.hasShiftDown() :
                tagString.contains(ToolControls.RIGHT_CLICK.toString()) ? !Screen.hasShiftDown() :
                        tagString.contains(ToolControls.BOTH.toString()));
    }
}
