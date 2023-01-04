package me.cuprize.collectors.commands;

import me.cuprize.collectors.Collectors;
import me.cuprize.collectors.files.LangManager;
import me.cuprize.collectors.util.Chat;
import me.cuprize.collectors.util.Console;
import me.cuprize.collectors.util.Item;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

            ItemStack collector = item.buildCollector();
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
                target.getInventory().addItem(collector);
            }
        }

        return false;
    }

}
