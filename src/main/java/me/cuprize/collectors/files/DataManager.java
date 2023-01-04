package me.cuprize.collectors.files;

import me.cuprize.collectors.Collectors;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataManager {
    public Collectors plugin;
    public DataManager(Collectors plugin) {
        this.plugin = plugin;
    }
    private static YamlConfiguration data;
    public void setUpDataFile(Collectors collectors) {
        File file = new File(this.plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            collectors.saveResource("data.yml", false);
        }
        data = YamlConfiguration.loadConfiguration(file);
    }
    public static String getString(String path) {
        return data.getString(path);
    }
}
