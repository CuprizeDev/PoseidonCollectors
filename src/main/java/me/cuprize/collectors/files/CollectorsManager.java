package me.cuprize.collectors.files;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTChunk;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.objects.Collector;
import me.cuprize.collectors.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CollectorsManager {

    public Collectors plugin;
    public CollectorsManager(Collectors plugin) {
        this.plugin = plugin;
    }

    public static HashMap<UUID, Chunk> locations = new HashMap<>();
    public static HashMap<ArmorStand, Location> holograms = new HashMap<>();

    public void addChunk(Player p, Chunk chunk) {
        locations.put(p.getUniqueId(), chunk);
    }

    public Chunk getChunk(Player p) {
        return locations.get(p.getUniqueId());
    }

    public void removeChunk(Player p) {
        locations.remove(p.getUniqueId());
    }

    public boolean isCollector(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);
        return nbtBlock.getData().getBoolean("collector");
    }

    public boolean isChunkTaken(Chunk chunk) {
        NBTChunk nbtChunk = new NBTChunk(chunk);
        return nbtChunk.getPersistentDataContainer().hasKey("collector");
    }

    public void addCollector(Collector collector) {
        NBTBlock nbtBlock = new NBTBlock(collector.getLocation().getBlock());
        NBTChunk nbtChunk = new NBTChunk(collector.getChunk());
        nbtChunk.getPersistentDataContainer().setBoolean("collector", true);
        nbtBlock.getData().setString("owner", String.valueOf(collector.getOwnerUUID()));
        nbtBlock.getData().setBoolean("collector", true);
    }

    public void removeCollector(Collector collector) {
        NBTChunk nbtChunk = new NBTChunk(collector.getChunk());
        NBTBlock nbtBlock = new NBTBlock(collector.getLocation().getBlock());
        nbtChunk.getPersistentDataContainer().removeKey("collector");
        nbtBlock.getData().removeKey("collector");
        nbtBlock.getData().removeKey("owner");

    }

    public Collector getCollector(Block block) {
        return new Collector(UUID.fromString(new NBTBlock(block).getData().getString("owner")), block.getLocation());
    }
}
