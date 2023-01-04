package me.cuprize.collectors.listeners;

import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.util.Chat;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.UUID;

public class BlockPlaceListener implements Listener {

    public Collectors plugin;
    public BlockPlaceListener(Collectors plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        HashMap<UUID, Chunk> chunks = new HashMap<>();

        Player p = e.getPlayer();
        Chunk chunk = e.getBlock().getLocation().getChunk();

        chunks.put(p.getUniqueId(), chunk);
        p.sendMessage(Chat.color("Chunk: " + chunks.get(p.getUniqueId())));

    }
}
