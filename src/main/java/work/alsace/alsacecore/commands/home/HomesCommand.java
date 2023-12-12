package work.alsace.alsacecore.commands.home;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.HomeDataLoader;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class HomesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.homes")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }

        Player player = (Player) sender;
        HomeDataLoader homeDataLoader = AlsaceCore.instance.homeProfiles.get(player.getUniqueId());

        List<TextComponent> homeComponents = homeDataLoader.getHomes().stream()
                .map(home -> createClickableHomeComponent(home))
                .collect(Collectors.toList());

        if (homeComponents.isEmpty()) {
            player.sendMessage(ChatColor.RED + "你没有任何传送点");
            return false;
        }

        StringJoiner joiner = new StringJoiner(ChatColor.GRAY + " | ");
        homeComponents.forEach(component -> joiner.add(component.toPlainText()));

        player.sendMessage(ChatColor.GRAY + "你的家: " + joiner.toString());

        return true;
    }

    private TextComponent createClickableHomeComponent(String home) {
        TextComponent homeComponent = new TextComponent(ChatColor.GOLD + home);
        homeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home));
        return homeComponent;
    }
}
