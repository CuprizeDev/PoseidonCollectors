package me.cuprize.collectors.files;

import me.cuprize.collectors.Collectors;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class DropsManager {
    public Collectors plugin;
    public DropsManager(Collectors plugin) {
        this.plugin = plugin;
    }
    private static YamlConfiguration drops;

    public void setUpDropsFile(Collectors collectors) {
        File file = new File(this.plugin.getDataFolder(), "drops.yml");
        if (!file.exists()) {
            collectors.saveResource("drops.yml", false);
        }
        drops = YamlConfiguration.loadConfiguration(file);
    }
    public static String getString(String path) {
        return drops.getString(path);
    }
}
