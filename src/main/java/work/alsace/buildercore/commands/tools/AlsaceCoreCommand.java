package work.alsace.buildercore.commands.tools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlsaceCoreCommand implements CommandExecutor, TabCompleter {
    private final BuilderCore plugin;

    private static final List<String> commands = Arrays.asList("reload", "info");

    public AlsaceCoreCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!sender.hasPermission("buildercore.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            switch (args[0]) {
                case "info" ->
                        sender.sendMessage(ChatColor.GRAY + "插件名称: §fAlsaceCore\n§7插件版本: §f" + plugin.getDescription().getVersion() + "\n§7插件作者: §f" + Arrays.toString(plugin.getDescription().getAuthors().toArray()));
                case "reload" -> {
                    plugin.loadConfig();
                    sender.sendMessage(ChatColor.GRAY + "重载成功");
                }
                default ->
                        sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/buildercore info §7- 查看插件信息\n§f/buildercore reload §7- 重载插件");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/buildercore info §7- 查看插件信息\n§f/buildercore reload §7- 重载插件");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (args.length == 1 && sender.hasPermission("buildercore.admin")) {
            return commands;
        }
        return new ArrayList<>(0);
    }
}
