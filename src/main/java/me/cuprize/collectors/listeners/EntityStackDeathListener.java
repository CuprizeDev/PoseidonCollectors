package me.cuprize.collectors.listeners;

import dev.rosewood.rosestacker.event.EntityStackMultipleDeathEvent;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityStackDeathListener implements Listener {

    public Collectors plugin;

    public EntityStackDeathListener(Collectors plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onMultipleStackDeath(EntityStackMultipleDeathEvent e) {
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        DropsManager dropsManager = new DropsManager(this.plugin);
        Chunk chunk = e.getStack().getLocation().getChunk();
        if (collectorsManager.isChunkTaken(chunk)) {
            for (Map.Entry<LivingEntity, EntityStackMultipleDeathEvent.EntityDrops> itemStack : e.getEntityDrops().entrySet()) {
                List<ItemStack> drops = itemStack.getValue().getDrops();
                for (int i = 0; drops.size() > i; i++) {
                    String dropName = String.valueOf(drops.get(i).getType());
                    if (dropsManager.isSellable(dropName)) {
                        int dropAmount = drops.get(i).getAmount();
                        dropsManager.addDrop(e.getStack().getLocation(), dropName, dropAmount);
                    }
                }
            }
            e.getEntityDrops().clear();
        }
    }
}
