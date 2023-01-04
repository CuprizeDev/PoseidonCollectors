package me.cuprize.collectors;

import me.cuprize.collectors.commands.CollectorCommand;
import me.cuprize.collectors.files.DataManager;
import me.cuprize.collectors.files.DropsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.listeners.BlockPlaceListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Collectors extends JavaPlugin {

    DropsManager dropsManager = new DropsManager(this);
    LangManager langManager = new LangManager(this);
    CollectorCommand collectorCommand = new CollectorCommand(this);
    BlockPlaceListener blockPlaceListener = new BlockPlaceListener(this);
    DataManager dataManager = new DataManager(this);

    @Override
    public void onEnable() {

        initiateConfig();
        initiateFiles();
        initiateCommands();
        initiateListeners();

    }

    @Override
    public void onDisable() {



    }


    private void initiateConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void initiateFiles() {
        langManager.setUpLangFile(this);
        dropsManager.setUpDropsFile(this);
        dataManager.setUpDataFile(this);
    }

    private void initiateCommands() {
        getCommand("collector").setExecutor(this.collectorCommand);
    }

    private void initiateListeners() {
        getServer().getPluginManager().registerEvents(this.blockPlaceListener, this);
    }

}
