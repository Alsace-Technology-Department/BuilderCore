// WarpsCommand.java
package work.alsace.buildercore.commands.warp;

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
import work.alsace.buildercore.service.WarpDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpsCommand implements CommandExecutor {
    private final BuilderCore plugin;

    public WarpsCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.warps")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }

        WarpDataLoader warpDataLoader = plugin.getWarpProfiles().get("warps");

        Map<String, List<String>> worldWarps = getWorldWarps(player);

        if (worldWarps.isEmpty()) {
            player.sendMessage(ChatColor.RED + "没有可用的传送点");
            return false;
        }

        player.sendMessage(ChatColor.GRAY + "可用传送点:");

        for (Map.Entry<String, List<String>> entry : worldWarps.entrySet()) {
            String worldName = entry.getKey();
            List<String> warps = entry.getValue();
            TextComponent message = new TextComponent(ChatColor.GRAY + worldName + ": ");
            for (String warp : warps) {
                TextComponent warpComponent = createClickableWarpComponent(warp);
                message.addExtra(warpComponent);
                message.addExtra(" §7| ");
            }

            player.spigot().sendMessage(message);
        }

        return true;
    }

    private Map<String, List<String>> getWorldWarps(Player sender) {
        Map<String, List<String>> worldWarps = new HashMap<>();
        plugin.getWarpProfiles().get("warps").getWarps().forEach(warp -> {
            String worldName = plugin.getWarpProfiles().get("warps").getWarpWorld(warp).getName();
            String alias = plugin.getWarpProfiles().get("warps").getWarpAlias(warp);

            if (sender.hasPermission("multiverse.access." + worldName)) {
                worldWarps.computeIfAbsent(worldName, k -> new ArrayList<>()).add(alias != null ? alias : warp);
            }
        });
        return worldWarps;
    }

    private TextComponent createClickableWarpComponent(String warp) {
        String realWarp = plugin.getWarpProfiles().get("warps").getRealWarpName(warp);

        TextComponent warpComponent = new TextComponent(ChatColor.GOLD + warp);
        warpComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + realWarp));
        TextComponent hoverText = new TextComponent("点击快捷传送");
        warpComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));
        return warpComponent;
    }
}
