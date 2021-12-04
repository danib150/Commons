package it.dbruni.commons.api;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class CommonsAPI {

    public static void sendTimeTitle(Player player, String title, String subtitle, int fadeIn, int time, int fadeOut) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

        PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle packetSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);

        PacketPlayOutTitle packetLength = new PacketPlayOutTitle(fadeIn, time, fadeOut);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetSubTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetLength);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

        PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle packetSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);

        PacketPlayOutTitle packetLength = new PacketPlayOutTitle(0, 40, 0);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetSubTitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetLength);
    }

    public static void sendGlobalTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");

        PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle packetSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);

        PacketPlayOutTitle packetLength = new PacketPlayOutTitle(fadeIn, time, fadeOut);

        forEachPlayer(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetTitle);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetSubTitle);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetLength);
        });
    }

    public static void forEachPlayer(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }
}
