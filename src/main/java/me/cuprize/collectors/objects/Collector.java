package me.cuprize.collectors.objects;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.UUID;

@SerializableAs("Collector")
public class Collector {

    Location location; // Location of the collector
    UUID ownerUUID; // Owner UUID
    UUID collectorUUID = UUID.randomUUID(); // Collector UUID

    public Collector(UUID ownerUUID, Location location) {
        this.location = location;
        this.ownerUUID = ownerUUID;
    }


    public Chunk getChunk() {
        return location.getChunk();
    }
    public World getWorld() {
        return location.getWorld();
    }
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    public UUID getCollectorUUID() {
        return collectorUUID;
    }
    public Location getLocation() {
        return location;
    }


}
