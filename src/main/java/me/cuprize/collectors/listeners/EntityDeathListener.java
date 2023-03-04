package me.cuprize.collectors.listeners;

import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class EntityDeathListener implements Listener {

    public Collectors plugin;
    public EntityDeathListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(EntityDeathEvent e) {
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        DropsManager dropsManager = new DropsManager(this.plugin);
        Chunk chunk = e.getEntity().getLocation().getChunk();
        if (collectorsManager.isChunkTaken(chunk)) {
            List<ItemStack> drops = e.getDrops();
            for (int i = 0; drops.size() > i; i++) {
                String dropName = String.valueOf(drops.get(i).getType());
                if (dropsManager.isSellable(dropName)) {
                    int dropAmount = drops.get(i).getAmount();
                    dropsManager.addDrop(e.getEntity().getLocation(), dropName, dropAmount);
                }
            }
            e.getDrops().clear();
        }
    }
}
