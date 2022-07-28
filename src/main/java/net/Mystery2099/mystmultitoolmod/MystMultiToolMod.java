package net.Mystery2099.mystmultitoolmod;

import com.mojang.logging.LogUtils;
import net.Mystery2099.mystmultitoolmod.config.MystMultiToolModClientConfig;
import net.Mystery2099.mystmultitoolmod.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MystMultiToolMod.MOD_ID)
public class MystMultiToolMod
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "mystmultitoolmod";
    public MystMultiToolMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MystMultiToolModClientConfig.SPEC, MOD_ID+"-client.toml");
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);

        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
    }
}
