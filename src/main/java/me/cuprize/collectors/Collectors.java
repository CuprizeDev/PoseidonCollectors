package me.cuprize.collectors;

import dev.rosewood.rosestacker.api.RoseStackerAPI;
import me.cuprize.collectors.commands.CollectorCommand;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.listeners.*;
import me.cuprize.collectors.util.Chat;
import me.cuprize.collectors.util.Console;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Collectors extends JavaPlugin {

    private static Economy eco;
    private static RoseStackerAPI rsAPI;
    public boolean roseStackerEnabled;
    public boolean shopGuiPlusEnabled;
    public boolean factionsEnabled;
    public boolean skyblockEnabled;
    DropsManager dropsManager = new DropsManager(this);
    LangManager langManager = new LangManager(this);
    CollectorCommand collectorCommand = new CollectorCommand(this);
    BlockPlaceListener blockPlaceListener = new BlockPlaceListener(this);
    BlockBreakListener blockBreakListener = new BlockBreakListener(this);
    CollectorsManager collectorsManager = new CollectorsManager(this);
    InteractListener interactListener = new InteractListener(this);
    InventoryListener inventoryListener = new InventoryListener(this);
    EntityDeathListener creatureSpawnListener = new EntityDeathListener(this);
    BlockGrowListener blockGrowListener = new BlockGrowListener(this);
    EntityExplodeListener blockExplosionListener = new EntityExplodeListener(this);
    EntityStackDeathListener entityStackDeathListener = new EntityStackDeathListener(this);


    @Override
    public void onEnable() {

        /*

        To Do List:
        - Fix Reload Issue
        - Add Holograms
        - Custom Enable Message / Disable Message
        - Add Custom Events
         */

        initiateConfig();
        initiateFiles();
        initiateCommands();
        initiateListeners();
        initiateShopGUIPlus();
        initiateFactions();
        initiateSkyblock();
        initiateEconomy();
        inititateRoseStacker();

    }

    private void initiateConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void initiateFiles() {
        langManager.setUpLangFile(this);
    }

    private void initiateCommands() {
        getCommand("collector").setExecutor(this.collectorCommand);
    }

    private void initiateListeners() {
        getServer().getPluginManager().registerEvents(this.blockPlaceListener, this);
        getServer().getPluginManager().registerEvents(this.blockBreakListener, this);
        getServer().getPluginManager().registerEvents(this.interactListener, this);
        getServer().getPluginManager().registerEvents(this.inventoryListener, this);
        getServer().getPluginManager().registerEvents(this.creatureSpawnListener, this);
        getServer().getPluginManager().registerEvents(this.blockGrowListener, this);
        getServer().getPluginManager().registerEvents(this.blockExplosionListener, this);
    }

    private void initiateShopGUIPlus() {
        if (Bukkit.getPluginManager().getPlugin("ShopGUIPlus") != null) {
            Console.sendMessage(Chat.color("&aShopGUIPlus was found!"));
            shopGuiPlusEnabled = true;
        } else {
            Console.sendMessage(Chat.color("&4ShopGUIPlus was not found!"));
            shopGuiPlusEnabled = false;
        }
    }

    private void initiateFactions() {
        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            Console.sendMessage(Chat.color("&aFactions was found!"));
            factionsEnabled = true;
        } else {
            Console.sendMessage(Chat.color("&4Factions was not found!"));
            factionsEnabled = false;
        }
    }

    private void initiateSkyblock() {
        if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) {
            Console.sendMessage(Chat.color("&aSky Block was found!"));
            skyblockEnabled = true;
        } else {
            Console.sendMessage(Chat.color("&4Sky Block was not found!"));
            skyblockEnabled = false;
        }
    }

    private void inititateRoseStacker() {
        if (Bukkit.getPluginManager().isPluginEnabled("RoseStacker")) {
            rsAPI = RoseStackerAPI.getInstance();
            getServer().getPluginManager().registerEvents(this.entityStackDeathListener, this);
            roseStackerEnabled = true;
        } else {
            roseStackerEnabled = false;
        }
    }

    private boolean initiateEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        eco = rsp.getProvider();
        return true;
    }

    public static Economy getEcon() {
        return eco;
    }

    public static RoseStackerAPI getRsAPI() {
        return rsAPI;
    }

}
