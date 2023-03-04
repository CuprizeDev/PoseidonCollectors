package me.cuprize.collectors.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import de.tr7zw.nbtapi.NBTBlock;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.menus.CollectorMenu;
import me.cuprize.collectors.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class InteractListener implements Listener {

    public Collectors plugin;
    public InteractListener(Collectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
        CollectorMenu collectorMenu = new CollectorMenu(this.plugin);
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (collectorsManager.isCollector(e.getClickedBlock())) {
                String uuid = new NBTBlock(e.getClickedBlock()).getData().getString("owner");

                // Skyblock Hook
                if (this.plugin.getConfig().getBoolean(
                        "settings.superior-skyblock-hook") && this.plugin.skyblockEnabled) {

                    if (SuperiorSkyblockAPI.getPlayer(p).getIsland() ==
                            SuperiorSkyblockAPI.getPlayer(UUID.fromString(uuid)).getIsland()) {
                        collectorMenu.open(p, e.getClickedBlock().getChunk());
                        collectorsManager.addChunk(p, e.getClickedBlock().getLocation().getChunk());
                    } else {
                        p.sendMessage(Chat.color(LangManager.getString("messages.wrong-island")));
                    }
                }

                // Factions Hook
                if (this.plugin.getConfig().getBoolean(
                        "settings.factions-hook") && this.plugin.factionsEnabled) {
                    FLocation fLocation = new FLocation(e.getClickedBlock().getLocation());
                    Faction faction = Board.getInstance().getFactionAt(fLocation);
                    if (!faction.isWilderness()) {
                        if (FPlayers.getInstance().getByPlayer(p).getFaction().equals(faction)) {
                            collectorMenu.open(p, e.getClickedBlock().getChunk());
                            collectorsManager.addChunk(p, e.getClickedBlock().getLocation().getChunk());
                        } else {
                            p.sendMessage(Chat.color(LangManager.getString("messages.wrong-faction")));
                        }
                    } else {
                        collectorMenu.open(p, e.getClickedBlock().getChunk());
                        collectorsManager.addChunk(p, e.getClickedBlock().getLocation().getChunk());
                    }
                }
                e.setCancelled(true);
            }
        }
    }
}
