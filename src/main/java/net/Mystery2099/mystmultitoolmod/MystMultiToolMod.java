package net.Mystery2099.mystmultitoolmod;

import com.mojang.logging.LogUtils;
import net.Mystery2099.mystmultitoolmod.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.Mystery2099.mystmultitoolmod.config.MystMultiToolModClientConfig;
import net.Mystery2099.mystmultitoolmod.item.ModItems;
import org.slf4j.Logger;

@Mod(MystMultiToolMod.MOD_ID)
public class MystMultiToolMod
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "mystmultitoolmod";

    public MystMultiToolMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MystMultiToolModClientConfig.SPEC, MOD_ID+"-client.toml");
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        PacketHandler.register(eventBus);

        eventBus.addListener(this::setup);



        // Register the enqueueIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        //// Register the processIMC method for modloading
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    //private void enqueueIMC(final InterModEnqueueEvent event)
    //{
    //    // Some example code to dispatch IMC to another mod
    //    InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    //}
    //
    //private void processIMC(final InterModProcessEvent event)
    //{
    //    // Some example code to receive and process InterModComms from other mods
    //    LOGGER.info("Got IMC {}", event.getIMCStream().
    //            map(m->m.messageSupplier().get()).
    //            collect(Collectors.toList()));
    //}
    //
    //// You can use SubscribeEvent and let the Event Bus discover methods to call
    //@SubscribeEvent
    //public void onServerStarting(ServerStartingEvent event)
    //{
    //    // Do something when the server starts
    //    LOGGER.info("HELLO from server starting");
    //}
    //
    //// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    //// Event bus for receiving Registry Events)
    //@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    //public static class RegistryEvents
    //{
    //    @SubscribeEvent
    //    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
    //    {
    //        // Register a new block here
    //        LOGGER.info("HELLO from Register Block");
    //    }
    //}

}
