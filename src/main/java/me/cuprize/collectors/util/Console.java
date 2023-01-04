package me.cuprize.collectors.util;

import static org.bukkit.Bukkit.getServer;

public class Console {
    public static void sendMessage(String a) {
        getServer().getConsoleSender().sendMessage(Chat.color(a));
    }
}
