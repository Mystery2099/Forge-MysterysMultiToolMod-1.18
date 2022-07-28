package net.Mystery2099.mystmultitoolmod.item.custom;

import com.google.common.collect.Sets;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModToolActions extends ToolActions{
    public static final Set<ToolAction> DEFAULT_MULTI_TOOL_ACTIONS = of(ToolActions.AXE_DIG, ToolActions.AXE_STRIP,
            ToolActions.AXE_SCRAPE, ToolActions.AXE_WAX_OFF, ToolActions.HOE_DIG, ToolActions.HOE_TILL, ToolActions.SHOVEL_DIG,
            ToolActions.SHOVEL_FLATTEN, ToolActions.PICKAXE_DIG);

    private static Set<ToolAction> of(ToolAction... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
