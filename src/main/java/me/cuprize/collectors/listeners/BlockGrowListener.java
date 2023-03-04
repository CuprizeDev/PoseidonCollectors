package me.cuprize.collectors.listeners;

import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener {

    public Collectors plugin;
    public BlockGrowListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        DropsManager dropsManager = new DropsManager(this.plugin);
        for (String type : this.plugin.getConfig().getConfigurationSection("menu.crops").getKeys(false)) {
            if (collectorsManager.isChunkTaken(e.getBlock().getChunk())) {
                if (e.getNewState().getType() == Material.getMaterial(type)) {
                    dropsManager.addDrop(e.getBlock().getLocation(), type, 1);
                    e.setCancelled(true);
                }
            }
        }
    }
}
