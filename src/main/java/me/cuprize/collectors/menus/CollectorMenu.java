package me.cuprize.collectors.menus;

import de.tr7zw.nbtapi.NBTChunk;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.DropsManager;
import me.cuprize.collectors.util.Chat;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CollectorMenu {

    public Collectors plugin;
    public CollectorMenu(Collectors plugin) {
        this.plugin = plugin;
    }

    public void open(Player p, Chunk chunk) {

        Inventory inv = Bukkit.createInventory(p, this.plugin.getConfig().getInt("menu.size"),
                Chat.color(this.plugin.getConfig().getString("menu.menu-name")));
        int fillerAmount = this.plugin.getConfig().getConfigurationSection("menu.fillers").getKeys(false).size();
        NumberFormat format = NumberFormat.getInstance();
        DropsManager dropsManager = new DropsManager(this.plugin);

        // Close Item

        ItemStack closeItem = new ItemStack(Material.valueOf(this.plugin.getConfig().getString(
                "menu.close.material")), 1);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(Chat.color(
                this.plugin.getConfig().getString("menu.close.name")));
        closeItem.setItemMeta(closeMeta);
        inv.setItem(this.plugin.getConfig().getInt("menu.close.slot"), closeItem);

        // Fill Item
        for (int i = 0; i < fillerAmount; i++) {
            ItemStack filler = new ItemStack(Material.valueOf(
                    this.plugin.getConfig().getString("menu.fillers." + i + ".item.material")),
                    1);
            ItemMeta fillerMeta = filler.getItemMeta();
            if (this.plugin.getConfig().getBoolean("menu.fillers." + i + ".item.force-glow")) {
                fillerMeta.addEnchant(Enchantment.LUCK, 5, true);
                fillerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            fillerMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.fillers." + i + ".name")));
            filler.setItemMeta(fillerMeta);
            int slot = this.plugin.getConfig().getInt("menu.fillers." + i + ".slot");
            inv.setItem(slot, filler);
        }


        // SELL ALL

        ItemStack sellAll = new ItemStack(Objects.requireNonNull(Material.getMaterial(
                Objects.requireNonNull(this.plugin.getConfig().getString("menu.sell-all.material")))));
        int allItemsCollected = dropsManager.getItemsInCollector(chunk);
        double allMoneyCollected = dropsManager.getMoneyInCollector(chunk);
        ItemMeta sellAllMeta = sellAll.getItemMeta();
        sellAllMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.sell-all.name")
                .replace("%money-amount%", format.format(allMoneyCollected))));
        List<String> sellAllLore = new LinkedList<>();
        for (String s : this.plugin.getConfig().getStringList("menu.sell-all.lore")) {
            sellAllLore.add(Chat.color(s)
                    .replace("%items-collected%", format.format(allItemsCollected))
                    .replace("%money-collected%", format.format(allMoneyCollected)));
        }
        sellAllMeta.setLore(sellAllLore);
        sellAll.setItemMeta(sellAllMeta);
        inv.setItem(this.plugin.getConfig().getInt("menu.sell-all.slot"), sellAll);

        // INDIVIDUAL MOB ITEMS

        for (String type: this.plugin.getConfig().getConfigurationSection(
                "menu.mob-drops").getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.getMaterial(type));
            ItemMeta itemMeta = itemStack.getItemMeta();
            NBTChunk nbtChunk = new NBTChunk(chunk);

            List<String> dropsLore = new LinkedList<>();

            if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig().getBoolean("settings.shop-gui-plus-hook")) {
                for (String s : this.plugin.getConfig().getStringList("menu.mob-drops." + type + ".lore")) {
                    dropsLore.add(Chat.color(s)
                            .replace("%items-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                            .replace("%money-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                    * ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type))))));
                }

                itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.mob-drops." + type + ".name")
                        .replace("%amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                        .replace("%money-amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                * ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))))));

            } else {

                for (String s : this.plugin.getConfig().getStringList("menu.mob-drops." + type + ".lore")) {
                    dropsLore.add(Chat.color(s)
                            .replace("%items-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                            .replace("%money-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                    * this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price"))));
                }

                itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.mob-drops." + type + ".name")
                        .replace("%amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                        .replace("%money-amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                * this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price")))));
            }

            if (this.plugin.getConfig().getBoolean("menu.mob-drops." + type + ".force-glow")) {
                itemMeta.addEnchant(Enchantment.LUCK, 5, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            itemMeta.setLore(dropsLore);
            itemStack.setItemMeta(itemMeta);
            inv.setItem(this.plugin.getConfig().getInt("menu.mob-drops." + type + ".slot"), itemStack);

        }

        // INDIVIDUAL CROP ITEMS

        for (String type: this.plugin.getConfig().getConfigurationSection(
                "menu.crops").getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.getMaterial(type));
            ItemMeta itemMeta = itemStack.getItemMeta();
            NBTChunk nbtChunk = new NBTChunk(chunk);

            List<String> dropsLore = new LinkedList<>();

            if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig()
                    .getBoolean("settings.shop-gui-plus-hook")) {
                for (String s : this.plugin.getConfig().getStringList("menu.crops." + type + ".lore")) {
                    dropsLore.add(Chat.color(s)
                            .replace("%items-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                            .replace("%money-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                    * ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type))))));
                    itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.crops." + type + ".name")
                            .replace("%amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                            .replace("%money-amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                    * ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))))));
                }
            } else {
                for (String s : this.plugin.getConfig().getStringList("menu.crops." + type + ".lore")) {
                    dropsLore.add(Chat.color(s)
                            .replace("%items-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                            .replace("%money-collected%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                    * this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price"))));
                }
                itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("menu.crops." + type + ".name")
                        .replace("%amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)))
                        .replace("%money-amount%", format.format(nbtChunk.getPersistentDataContainer().getInteger(type)
                                * this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price")))));
            }

            if (this.plugin.getConfig().getBoolean("menu.crops." + type + ".force-glow")) {
                itemMeta.addEnchant(Enchantment.LUCK, 5, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            itemMeta.setLore(dropsLore);
            itemStack.setItemMeta(itemMeta);
            inv.setItem(this.plugin.getConfig().getInt("menu.crops." + type + ".slot"), itemStack);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                p.openInventory(inv);
            }
        }.runTaskLater(plugin, 8);
    }
}
