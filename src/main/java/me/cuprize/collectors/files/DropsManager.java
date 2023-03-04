package me.cuprize.collectors.files;

import de.tr7zw.nbtapi.NBTChunk;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.events.SellEvent;
import me.cuprize.collectors.util.Chat;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Objects;

public class DropsManager {
    public Collectors plugin;
    public DropsManager(Collectors plugin) {
        this.plugin = plugin;
    }
    NumberFormat format = NumberFormat.getInstance();

    public void sellDrop(Player p, String type, Chunk chunk) {
        int amount;
        Economy economy = Collectors.getEcon();
        double price;
        if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig()
                .getBoolean("settings.shop-gui-plus-hook")) {
            amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
            price = ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(
                    Objects.requireNonNull(Material.getMaterial(type))))*amount;
            economy.depositPlayer(p, price);
        } else {
            if (this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false).contains(type)) {
                amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                price = this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price")*amount;
                economy.depositPlayer(p, price);
            }
            if (this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false).contains(type)) {
                amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                price = this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price")*amount;
                economy.depositPlayer(p, price);
            }
        }
        new NBTChunk(chunk).getPersistentDataContainer().removeKey(type);
    }

    public void sellSingleDrop(Player p, String type, Chunk chunk) {
        int amount = 0;
        double sellAmount = 0;
        Economy economy = Collectors.getEcon();
        if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig()
                .getBoolean("settings.shop-gui-plus-hook")) {
            amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
            sellAmount = ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(
                    Objects.requireNonNull(Material.getMaterial(type))))*amount;
            economy.depositPlayer(p, sellAmount);
        } else {
            if (this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false).contains(type)) {
                amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                sellAmount = this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price")*amount;
                economy.depositPlayer(p, sellAmount);
            }
            if (this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false).contains(type)) {
                amount = new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                sellAmount = this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price")*amount;
                economy.depositPlayer(p, sellAmount);
            }
        }
        new NBTChunk(chunk).getPersistentDataContainer().removeKey(type);
        Bukkit.getPluginManager().callEvent(new SellEvent(p, sellAmount, amount));
    }

    public boolean isSellable(String type) {
        return this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false).contains(type)
                || this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false).contains(type);
    }

    public void sellAllDrops(Player p, Chunk chunk) {
        double sellAmount = 0;
        if (this.plugin.getConfig().getBoolean("settings.shop-gui-plus-hook") && this.plugin.shopGuiPlusEnabled) {
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
                if (!type.equals("TNT")) {
                    sellAmount = sellAmount +
                            (ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                    * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type));
                    if (new NBTChunk(chunk).getPersistentDataContainer().hasKey(type)) {
                        sellDrop(p, type, chunk);
                    }
                }
            }
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
                sellAmount = sellAmount +
                        (ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type));
                if (new NBTChunk(chunk).getPersistentDataContainer().hasKey(type)) {
                    sellDrop(p, type, chunk);
                }
            }
        } else {
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
                if (!type.equals("TNT")) {
                    sellAmount = sellAmount + this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price") *
                            new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                    if (new NBTChunk(chunk).getPersistentDataContainer().hasKey(type)) {
                        sellDrop(p, type, chunk);
                    }
                }
            }
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
                sellAmount = sellAmount + this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price") *
                        new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                if (new NBTChunk(chunk).getPersistentDataContainer().hasKey(type)) {
                    sellDrop(p, type, chunk);
                }
            }
        }
        p.sendMessage(Chat.color(LangManager.getString("messages.sell-all").replace("%amount%",
                format.format(sellAmount))));
        Bukkit.getPluginManager().callEvent(new SellEvent(p, sellAmount,
                getItemsInCollector(chunk)-new NBTChunk(chunk).
                        getPersistentDataContainer().getInteger("TNT")));
    }

    public void addDrop(Location location, String type, int amount) {
        NBTChunk nbtChunk = new NBTChunk(location.getChunk());
        if (nbtChunk.getPersistentDataContainer().hasKey(type)) {
            nbtChunk.getPersistentDataContainer().setInteger(type, amount +
                    nbtChunk.getPersistentDataContainer().getInteger(type));
        } else {
            nbtChunk.getPersistentDataContainer().setInteger(type, amount);
        }
    }

    public void clearDrops(Chunk chunk) {
        NBTChunk nbtChunk = new NBTChunk(chunk);
        for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
            if (nbtChunk.getPersistentDataContainer().hasKey(type)) {
                nbtChunk.getPersistentDataContainer().removeKey(type);
            }
        }
        for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
            if (nbtChunk.getPersistentDataContainer().hasKey(type)) {
                nbtChunk.getPersistentDataContainer().removeKey(type);
            }
        }
    }

    public int getItemsInCollector(Chunk chunk) {
        int itemsAmount = 0;
        for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
            itemsAmount = itemsAmount+new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
        }
        for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
            itemsAmount = itemsAmount+new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
        }
        return itemsAmount;
    }

    public double getMoneyInCollector(Chunk chunk) {
        double sellAmount = 0;
        if (this.plugin.getConfig().getBoolean("settings.shop-gui-plus-hook") && this.plugin.shopGuiPlusEnabled) {
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
                if (!type.equals("TNT")) {
                    sellAmount = sellAmount +
                            (ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                    * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type));
                }
            }
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
                sellAmount = sellAmount +
                        (ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type));
            }
        } else {
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.mob-drops").getKeys(false)) {
                if (!type.equals("TNT")) {
                    sellAmount = sellAmount + this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price")
                            * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
                }
            }
            for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
                sellAmount = sellAmount + this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price")
                        * new NBTChunk(chunk).getPersistentDataContainer().getInteger(type);
            }
        }
            return sellAmount;
    }
}
