package me.cuprize.collectors.util;

import de.tr7zw.nbtapi.NBTItem;
import me.cuprize.collectors.Collectors;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Item {

    private final Collectors plugin;
    public Item(Collectors plugin) { this.plugin = plugin; }

    public ItemStack buildCollector() {
        ItemStack itemStack = new ItemStack(Material.BEACON);
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setBoolean("collector", true);
        nbtItem.applyNBT(itemStack);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("collector-item.name")));

        List<String> uncoloredLore = this.plugin.getConfig().getStringList("collector-item.lore");
        List<String> coloredLore = new LinkedList<>();
        for (String s: uncoloredLore) {
            coloredLore.add(Chat.color(s));
        }
        itemMeta.setLore(coloredLore);

        if (this.plugin.getConfig().getBoolean("collector-item.force-glow")) {
            itemMeta.addEnchant(Enchantment.LUCK, 5, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
