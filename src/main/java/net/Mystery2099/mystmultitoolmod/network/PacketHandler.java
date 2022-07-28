package net.Mystery2099.mystmultitoolmod.network;

import net.Mystery2099.mystmultitoolmod.MystMultiToolMod;
import net.Mystery2099.mystmultitoolmod.network.packets.IMystMultiToolModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Function;

public final class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(3);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(MystMultiToolMod.rl("main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int index;

    public static void register(IEventBus bus) {
    }

    private static <MSG extends IMystMultiToolModPacket> void registerClientToServer(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder) {
        registerMessage(type, decoder, NetworkDirection.PLAY_TO_SERVER);
    }

    private static <MSG extends IMystMultiToolModPacket> void registerServerToClient(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder) {
        registerMessage(type, decoder, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <MSG extends IMystMultiToolModPacket> void registerMessage(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder, NetworkDirection networkDirection) {
        HANDLER.registerMessage(index++, type, IMystMultiToolModPacket::encode, decoder, IMystMultiToolModPacket::handle, Optional.of(networkDirection));
    }

    public static <MSG extends IMystMultiToolModPacket> void sendToServer(MSG msg) {
        HANDLER.sendToServer(msg);
    }
}
