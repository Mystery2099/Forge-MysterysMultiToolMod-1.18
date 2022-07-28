package net.Mystery2099.mystmultitoolmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMystMultiToolModPacket {
    void handle(NetworkEvent.Context context);

    void encode(FriendlyByteBuf buffer);

    static <PACKET extends IMystMultiToolModPacket> void handle(final PACKET message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> message.handle(context));
        context.setPacketHandled(true);
    }
}
