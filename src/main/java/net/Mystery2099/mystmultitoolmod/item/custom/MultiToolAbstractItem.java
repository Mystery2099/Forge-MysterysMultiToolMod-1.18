package net.Mystery2099.mystmultitoolmod.item.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.Mystery2099.mystmultitoolmod.util.tags.ModTags;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MultiToolAbstractItem extends DiggerItem {
    public MultiToolAbstractItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, ModTags.ModBlockTags.MINEABLE_WITH_MULTITOOL, pProperties);
    }
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        BlockState blockstate = level.getBlockState(blockPos);

        //Axe functionality
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_STRIP, false));
        Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_SCRAPE, false));
        Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_WAX_OFF, false));
        ItemStack itemstack = pContext.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            optional3 = optional;
        } else if (optional1.isPresent()) {
            level.playSound(player, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockPos, 0);
            optional3 = optional1;
        } else if (optional2.isPresent()) {
            level.playSound(player, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, blockPos, 0);
            optional3 = optional2;
        }

        if (optional3.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, itemstack);
            }

            level.setBlock(blockPos, optional3.get(), 11);
            if (player != null) {
                itemstack.hurtAndBreak(1, player, (p_150686_) -> p_150686_.broadcastBreakEvent(pContext.getHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        //Hoe Functionality
        if (!player.isShiftKeyDown()) {
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
                        if (player != null) {
                            pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> player.broadcastBreakEvent(pContext.getHand()));
                        }
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        //Shovel Functionality
        else if (player.isShiftKeyDown()) {

            if (pContext.getClickedFace() == Direction.DOWN) {
                return InteractionResult.PASS;
            } else {
                BlockState blockstate1 = blockstate.getToolModifiedState(pContext, ToolActions.SHOVEL_FLATTEN, false);
                BlockState blockstate2 = null;
                if (blockstate1 != null && level.isEmptyBlock(blockPos.above())) {
                    level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    blockstate2 = blockstate1;
                } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                    if (!level.isClientSide()) {
                        level.levelEvent(null, 1009, blockPos, 0);
                    }

                    CampfireBlock.dowse(pContext.getPlayer(), level, blockPos, blockstate);
                    blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.FALSE);
                }

                if (blockstate2 != null) {
                    if (!level.isClientSide) {
                        level.setBlock(blockPos, blockstate2, 11);
                        if (player != null) {
                            pContext.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(pContext.getHand()));
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static Consumer<UseOnContext> changeIntoState(BlockState pState) {
        return (pContext) -> pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
    }

    public static Consumer<UseOnContext> changeIntoStateAndDropItem(BlockState pState, ItemLike pItemToDrop) {
        return (pContext) -> {
            pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
            Block.popResourceFromFace(pContext.getLevel(), pContext.getClickedPos(), pContext.getClickedFace(), new ItemStack(pItemToDrop));
        };
    }

    public static boolean onlyIfAirAbove(UseOnContext pContext) {
        return pContext.getClickedFace() != Direction.DOWN && pContext.getLevel().getBlockState(pContext.getClickedPos().above()).isAir();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ModToolActions.DEFAULT_MULTI_TOOL_ACTIONS.contains(toolAction);
    }
}
