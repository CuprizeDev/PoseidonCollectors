package me.cuprize.collectors.files;

import me.cuprize.collectors.Collectors;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class LangManager {

    public Collectors plugin;
    public LangManager(Collectors plugin) {
        this.plugin = plugin;
    }
    private static YamlConfiguration lang;

    public void setUpLangFile(Collectors collectors) {

        File file = new File(this.plugin.getDataFolder(), "lang.yml");

        if (!file.exists()) {
            collectors.saveResource("lang.yml", false);
        }
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public static String getString(String path) {
        return lang.getString(path);
    }

    public static List<String> getStringList(String path) {
        return lang.getStringList(path);
    }

}
