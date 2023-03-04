package me.cuprize.collectors.listeners;

import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.objects.Collector;
import me.cuprize.collectors.util.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class EntityExplodeListener implements Listener {

    public Collectors plugin;
    public EntityExplodeListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void blockExplosion(EntityExplodeEvent e) {
        List<Block> blocks = e.blockList();
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        for (Block block: blocks) {
            if (block.getType() == Material.BEACON) {
                if (collectorsManager.isCollector(block)) {
                    block.setType(Material.AIR);
                    Item item = new Item(this.plugin);
                    block.getLocation().getWorld().dropItemNaturally(e.getLocation(), item.buildCollector());
                    Collector collector = collectorsManager.getCollector(block);
                    collectorsManager.removeCollector(collector);
                }
            }
        }
    }
}
