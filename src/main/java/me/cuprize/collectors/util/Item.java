package me.cuprize.collectors.util;

import me.cuprize.collectors.Collectors;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

    private final Collectors plugin;
    public Item(Collectors plugin) { this.plugin = plugin; }


    public String getVersion(String tag) {

        String version = "Default";
        switch (tag) {
            case "bob":
                version = "1.8";
                return version;
            case "zig":
                version = "1.9";
                return version;
        }
        return version;
    }

    public ItemStack buildCollector() {
        ItemStack itemStack = new ItemStack(Material.BEACON);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Chat.color(this.plugin.getConfig().getString("collector-item.name")));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public boolean isCollector(ItemStack itemStack) {

        return true;
    }
}
