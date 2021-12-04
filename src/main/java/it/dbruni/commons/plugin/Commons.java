package it.dbruni.commons.plugin;

import it.dbruni.commons.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class Commons extends JavaPlugin {



    @Getter
    @Setter
    private static Commons instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        if (!checkDependancies()) {
            return;
        }

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Object header = new ChatComponentText(PlaceholderAPI.setPlaceholders(p, getInstance().getConfig().getString("main-configuration.tablist.header")));
                        Object footer = new ChatComponentText(PlaceholderAPI.setPlaceholders(p, getInstance().getConfig().getString("main-configuration.tablist.footer")));

                        a.set(packet, header);
                        b.set(packet, footer);

                        if (Bukkit.getOnlinePlayers().size() == 0) return;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                        }

                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0, 20);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[" + StringUtils.commonsname + "] " + "Abilitato commons versione: " + StringUtils.version + " by " + StringUtils.author);

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + StringUtils.commonsname + "] " + "Disabilitato commons versione: " + StringUtils.version + " by " + StringUtils.author);

    }

    private boolean checkDependancies() {
        return checkDependancy("PlaceholderAPI");
    }

    private boolean checkDependancy(String pluginName) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            return true;
        } else {
            criticalShutdown("Richiesto " + pluginName);
            return false;
        }
    }

    private void criticalShutdown(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] " + message);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) { }
        setEnabled(false);
        Bukkit.shutdown();
    }

}
