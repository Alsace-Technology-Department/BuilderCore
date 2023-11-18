package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import work.alsace.alsacecore.AlsaceCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlsaceCoreCommand implements CommandExecutor, TabCompleter {
    private final AlsaceCore plugin;

    private static final List<String> commands = Arrays.asList("reload", "info");

    public AlsaceCoreCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!sender.hasPermission("alsace.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        switch (args[0]) {
            case "info" -> {
                // TODO 插件信息
                sender.sendMessage(ChatColor.GRAY + "插件名称: §fAlsaceCore\n§7插件版本: §f" + plugin.getDescription().getVersion() + "\n§7插件作者: §f" + Arrays.toString(plugin.getDescription().getAuthors().toArray()));
            }
            case "reload" -> {
                plugin.loadConfig();
                sender.sendMessage(ChatColor.GRAY + "重载成功");
            }
            default -> {
                sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/alsacecore info §7- 查看插件信息\n§f/alsacecore reload §7- 重载插件");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1 && sender.hasPermission("alsace.admin")) {
            return commands;
        }
        return new ArrayList<>(0);
    }
}
