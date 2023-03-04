package me.cuprize.collectors.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.objects.Collector;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.util.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    public Collectors plugin;
    public BlockPlaceListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);

        Player p = e.getPlayer();
        Location location = e.getBlock().getLocation();
        NBTItem nbtItem = new NBTItem(e.getItemInHand());
        Collector collector = new Collector(e.getPlayer().getUniqueId(), location);

        if (nbtItem.getBoolean("collector")) {
            if (collectorsManager.isChunkTaken(location.getChunk())) {
                p.sendMessage(Chat.color(LangManager.getString("messages.chunk-used")));
                e.setCancelled(true);
            } else {
                p.sendMessage(Chat.color(LangManager.getString("messages.collector-placed")));
                collectorsManager.addCollector(collector);
            }
        }
    }
}
