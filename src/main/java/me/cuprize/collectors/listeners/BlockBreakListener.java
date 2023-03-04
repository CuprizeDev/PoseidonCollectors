package me.cuprize.collectors.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.CollectorsManager;
import me.cuprize.collectors.files.DropsManager;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.objects.Collector;
import me.cuprize.collectors.util.Chat;
import me.cuprize.collectors.util.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    public Collectors plugin;
    public BlockBreakListener(Collectors plugin) {
        this.plugin = plugin;
    }

   @EventHandler
   public void onBreak(BlockBreakEvent e) {
       CollectorsManager collectorsManager = new CollectorsManager(this.plugin);
       DropsManager dropsManager = new DropsManager(this.plugin);
       Player p = e.getPlayer();
       Block block = e.getBlock();
       Location location = block.getLocation();
       Collector collector = new Collector(e.getPlayer().getUniqueId(), location);
       Item item = new Item(this.plugin);
       if (collectorsManager.isCollector(block)) {

           // Sky Block Hook

           if (this.plugin.getConfig().getBoolean(
                   "settings.superior-skyblock-hook") && this.plugin.skyblockEnabled) {

               if (SuperiorSkyblockAPI.getPlayer(p).getIsland()
                       .equals(SuperiorSkyblockAPI.getIslandAt(e.getPlayer().getLocation()))) {
                   e.setCancelled(true);
                   collectorsManager.removeCollector(collector);
                   dropsManager.clearDrops(block.getChunk());
                   block.setType(Material.AIR);
                   p.sendMessage(Chat.color(LangManager.getString("messages.collector-destroyed")));
                   p.getInventory().addItem(new ItemStack(item.buildCollector()));
               } else {
                   p.sendMessage(Chat.color(LangManager.getString("messages.wrong-faction")));
                   e.setCancelled(true);
               }
           }

           // Factions Hook

           if (this.plugin.getConfig().getBoolean(
                   "settings.factions-hook") && this.plugin.factionsEnabled) {
               FLocation fLocation = new FLocation(e.getBlock().getLocation());
               Faction faction = Board.getInstance().getFactionAt(fLocation);
               if (!faction.isWilderness()) {
                   if (FPlayers.getInstance().getByPlayer(p).getFaction().equals(faction)) {
                       e.setCancelled(true);
                       collectorsManager.removeCollector(collector);
                       dropsManager.clearDrops(block.getChunk());
                       block.setType(Material.AIR);
                       p.sendMessage(Chat.color(LangManager.getString("messages.collector-destroyed")));
                       p.getInventory().addItem(new ItemStack(item.buildCollector()));
                   } else {
                       p.sendMessage(Chat.color(LangManager.getString("messages.wrong-faction")));
                       e.setCancelled(true);
                   }
               } else {
                   e.setCancelled(true);
                   collectorsManager.removeCollector(collector);
                   dropsManager.clearDrops(block.getChunk());
                   block.setType(Material.AIR);
                   p.sendMessage(Chat.color(LangManager.getString("messages.collector-destroyed")));
                   p.getInventory().addItem(new ItemStack(item.buildCollector()));
               }
           }
       }
    }
}
