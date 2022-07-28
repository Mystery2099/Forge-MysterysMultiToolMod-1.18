package net.Mystery2099.mystmultitoolmod.config;

import net.Mystery2099.mystmultitoolmod.util.enums.ChangeToolModel;
import net.Mystery2099.mystmultitoolmod.util.enums.ToolControls;
import net.minecraftforge.common.ForgeConfigSpec;
import net.Mystery2099.mystmultitoolmod.util.enums.DefaultToolModel;

public class MystMultiToolModClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    //Tool Appearance
    public static final ForgeConfigSpec.EnumValue DEFAULT_TOOL_MODEL;
    public static final ForgeConfigSpec.BooleanValue DYNAMIC_TOOL_MODEL;
    public static final ForgeConfigSpec.BooleanValue CHANGE_MODEL_WHEN_USING_MULTITOOL_ON_A_BLOCK;
    public static final ForgeConfigSpec.BooleanValue CHANGE_MODEL_ON_HITTING_A_BLOCK_WITH_MULTITOOL;

    //Tool Controls
    public static final ForgeConfigSpec.EnumValue STRIPPING;
    public static final ForgeConfigSpec.EnumValue TILLING;
    public static final ForgeConfigSpec.EnumValue FLATTENING;





    static {
        BUILDER.push("Configs for Mystery's MultiTool Mod");

        //Tool Appearance
        DEFAULT_TOOL_MODEL = BUILDER.comment("Change the default tool model").defineEnum("Default Tool Model", DefaultToolModel.HAMMER);
        DYNAMIC_TOOL_MODEL = BUILDER.comment("Enable or disable dynamic MultiTool models").define("Dynamic Tool Model", true);
        CHANGE_MODEL_WHEN_USING_MULTITOOL_ON_A_BLOCK = BUILDER.comment("Toggle whether or not the MultiTool will change when using it on a block").define("Cycle Through Tools While Using a MultiTool On a Block", true);
        CHANGE_MODEL_ON_HITTING_A_BLOCK_WITH_MULTITOOL = BUILDER.comment("Toggle whether or not the MultiTool will change when hitting a block with it").define("Cycle Through Tools While Hitting a Block With a MultiTool", true);

        //Controls
        STRIPPING = BUILDER.comment("Disable or change controls for Stripping with MultiTools").defineEnum("Stripping", ToolControls.BOTH);
        TILLING = BUILDER.comment("Disable or change controls for Tilling with MultiTools").defineEnum("Tilling", ToolControls.RIGHT_CLICK);
        FLATTENING = BUILDER.comment("Disable or change controls for Flattening with MultiTools").defineEnum("Flattening", ToolControls.RIGHT_CLICK_AND_PRESS_SHIFT);



        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
