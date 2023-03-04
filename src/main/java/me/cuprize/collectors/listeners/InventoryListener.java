package me.cuprize.collectors.listeners;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import de.tr7zw.nbtapi.NBTChunk;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.objects.Collector;
import me.cuprize.collectors.util.Chat;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.NumberFormat;

public class InventoryListener implements Listener {

    public Collectors plugin;
    public InventoryListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        DropsManager dropsManager = new DropsManager(this.plugin);
        NBTChunk nbtChunk = new NBTChunk(collectorsManager.getChunk(p));
        NumberFormat format = NumberFormat.getInstance();

        if (e.getView().getTitle().equals(Chat.color(
                this.plugin.getConfig().getString("menu.menu-name")))) {

            // Close Slot

            if (e.getRawSlot() == this.plugin.getConfig().getInt("menu.close.slot")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.closeInventory();
                    }
                }.runTaskLater(plugin, 1);
            }

            // Sell All Slot

            if (e.getRawSlot() == this.plugin.getConfig().getInt("menu.sell-all.slot")) {
                dropsManager.sellAllDrops(p, collectorsManager.getChunk(p));
                p.closeInventory();
                collectorsManager.removeChunk(p);
            }

            // Mob Drops

            for (String type: this.plugin.getConfig().getConfigurationSection("menu.mob-drops").
                    getKeys(false)) {
                if (e.getRawSlot() == this.plugin.getConfig().getInt(
                        "menu.mob-drops." + type + ".slot")) {
                    if (nbtChunk.getPersistentDataContainer().hasKey(type)) {
                            if (!Material.getMaterial(type).equals(Material.TNT) && (this.plugin.getConfig().getBoolean(
                                    "settings.factions-hook") && this.plugin.factionsEnabled)) {
                                Double sellAmount;
                                if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig()
                                        .getBoolean("settings.shop-gui-plus-hook")) {
                                    sellAmount = ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                            * new NBTChunk(collectorsManager.getChunk(p)).getPersistentDataContainer().getInteger(type);
                                } else {
                                    sellAmount = this.plugin.getConfig().getDouble("menu.mob-drops." + type + ".sell-price")
                                            * new NBTChunk(collectorsManager.getChunk(p)).getPersistentDataContainer().getInteger(type);
                                }
                                dropsManager.sellSingleDrop(p, type, collectorsManager.getChunk(p));
                                collectorsManager.removeChunk(p);
                                p.sendMessage(Chat.color(LangManager.getString("messages.sell-item").replace("%amount%",
                                        format.format(sellAmount))));
                                p.closeInventory();
                            } else {
                                int amount = nbtChunk.getPersistentDataContainer().getInteger("TNT");
                                Faction faction = FPlayers.getInstance().getByPlayer(p).getFaction();
                                if (!FPlayers.getInstance().getByPlayer(p).getFaction().isWilderness()) {
                                    if (faction.getTnt() + amount < faction.getTntBankLimit()) {
                                        p.sendMessage(Chat.color(LangManager.getString("messages.tnt-deposited")
                                                .replace("%amount%", format.format(amount))));
                                        faction.addTnt(amount);
                                        dropsManager.clearDrops(collectorsManager.getChunk(p));
                                        collectorsManager.removeChunk(p);
                                        p.closeInventory();
                                    } else {
                                        amount = faction.getTntBankLimit() - faction.getTnt();
                                        if (!(amount == 0)) {
                                            p.sendMessage(Chat.color(LangManager.getString("messages.too-much-tnt"))
                                                    .replace("%amount%", format.format(amount)));
                                            nbtChunk.getPersistentDataContainer().setInteger("TNT",
                                                    nbtChunk.getPersistentDataContainer().getInteger("TNT") - amount);
                                            faction.addTnt(amount);
                                        } else {
                                            p.sendMessage(Chat.color(LangManager.getString("messages.tnt-full")));
                                        }
                                        collectorsManager.removeChunk(p);
                                        p.closeInventory();
                                    }
                                } else {
                                    p.sendMessage(Chat.color(LangManager.getString("messages.cant-deposit-to-wild")));
                                }
                            }
                    } else {
                        p.sendMessage(Chat.color(LangManager.getString("messages.empty")));
                    }
                }
            }

            // Crop Drops

            for (String type: this.plugin.getConfig().getConfigurationSection("menu.crops").
                    getKeys(false)) {
                if (e.getRawSlot() == this.plugin.getConfig().getInt(
                        "menu.crops." + type + ".slot")) {
                    if (nbtChunk.getPersistentDataContainer().hasKey(type)) {
                        double sellAmount;
                        if (this.plugin.shopGuiPlusEnabled && this.plugin.getConfig()
                                .getBoolean("settings.shop-gui-plus-hook")) {
                            sellAmount = ShopGuiPlusApi.getItemStackPriceSell(new ItemStack(Material.getMaterial(type)))
                                    * new NBTChunk(collectorsManager.getChunk(p)).getPersistentDataContainer().getInteger(type);
                        } else {
                            sellAmount = this.plugin.getConfig().getDouble("menu.crops." + type + ".sell-price")
                                    * new NBTChunk(collectorsManager.getChunk(p)).getPersistentDataContainer().getInteger(type);
                        }
                        dropsManager.sellSingleDrop(p, type, collectorsManager.getChunk(p));
                        collectorsManager.removeChunk(p);
                        p.sendMessage(Chat.color(LangManager.getString("messages.sell-item").replace("%amount%",
                                format.format(sellAmount))));
                        p.closeInventory();
                    } else {
                        p.sendMessage(Chat.color(LangManager.getString("messages.empty")));
                    }
                }
            }

            e.setCancelled(true);
        }
    }
}
