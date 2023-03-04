package me.cuprize.collectors.commands;

import com.massivecraft.factions.FPlayers;
import de.tr7zw.nbtapi.NBTChunk;
import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.util.Chat;
import me.cuprize.collectors.util.Console;
import me.cuprize.collectors.util.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CollectorCommand implements CommandExecutor {

    public Collectors plugin;
    public CollectorCommand(Collectors plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Item item = new Item(this.plugin);

        if (args.length < 1) {
            sender.sendMessage(Chat.color(LangManager.getString("messages.invalid-arg")));
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            List<String> list = LangManager.getStringList("messages.help");
            for (String message : list)
                sender.sendMessage(Chat.color(message));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {

            if (args.length < 2) {
                sender.sendMessage(Chat.color(LangManager.getString("messages.invalid-arg")));
                return true;
            }

            if (!sender.hasPermission("collectors.give") ||
                    !sender.hasPermission("collectors.*")) {
                Console.sendMessage(Chat.color(LangManager.getString("messages.no-permission")));
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (!(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1])))) {
                    Console.sendMessage(LangManager.getString("messages.offline"));
                } else {
                    target.sendMessage(Chat.color(LangManager.getString("messages.given-collector")));
                    sender.sendMessage(Chat.color(LangManager.getString("messages.gave-collector")
                            .replace("%player%", target.getName())));
                }
                target.getInventory().addItem(item.buildCollector());
            }
        }

        if (args[0].equals("read")) {

            Player player = (Player) sender;

            NBTChunk nbtChunk = new NBTChunk(player.getLocation().getChunk());
            nbtChunk.getPersistentDataContainer().removeKey("collector");
            player.sendMessage("Keys" + nbtChunk.getPersistentDataContainer().getKeys());
            player.sendMessage(Chat.color("Test Cactus: " + nbtChunk.getPersistentDataContainer().getInteger("CACTUS")));
            player.sendMessage(Chat.color("Test Carrots: " + nbtChunk.getPersistentDataContainer().getInteger("POTATO")));
        }
        return false;
    }
}
