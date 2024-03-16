package work.alsace.buildercore.commands.home;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;
import work.alsace.buildercore.service.HomeDataLoader;

import java.util.List;
import java.util.stream.Collectors;

public class HomesCommand implements CommandExecutor {
    private final BuilderCore plugin;

    public HomesCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.homes")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }

        HomeDataLoader homeDataLoader = plugin.getHomeProfiles().get(player.getUniqueId());

        List<BaseComponent> homeComponents = homeDataLoader.getHomes().stream()
                .map(this::createClickableHomeComponent)
                .collect(Collectors.toList());

        if (homeComponents.isEmpty()) {
            player.sendMessage(ChatColor.RED + "你没有任何传送点");
            return false;
        }

        TextComponent message = new TextComponent(ChatColor.GRAY + "你的家: ");
        for (BaseComponent component : homeComponents) {
            message.addExtra(component);
            message.addExtra(" §7| ");
        }

        player.spigot().sendMessage(message);

        return true;
    }

    private TextComponent createClickableHomeComponent(String home) {
        TextComponent homeComponent = new TextComponent(ChatColor.GOLD + home);
        homeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home));
        TextComponent hoverText = new TextComponent("点击快捷传送");
        homeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));
        return homeComponent;
    }
}
