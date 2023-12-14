package com.puddingkc.commands.essentials;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WhoisCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/whois <玩家> §7- 查询指定玩家信息";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.whois")) {
            Player player = sender.getServer().getPlayer(strings[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "指定的玩家不在线或不存在");
                return false;
            }

            Location location = player.getLocation();
            TextComponent Component = new TextComponent("§7UUID: §a" + player.getUniqueId());
            Component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getUniqueId().toString()));
            TextComponent hoverText = new TextComponent("点击复制");
            Component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText}));

            sender.sendMessage("§7用户名: §f" + player.getName());
            player.spigot().sendMessage(Component);
            sender.sendMessage("§7IP地址: §f" + player.getAddress().getHostString());
            sender.sendMessage("§7OP权限: §f" + player.isOp());
            sender.sendMessage("§7游戏模式: §f" + player.getGameMode());
            sender.sendMessage("§7坐标位置: §fX:" + Math.floor(location.getX()) + " Y:" + Math.floor(location.getY()) + " Z:" + Math.floor(location.getZ()));
            sender.sendMessage("§7所在世界: §f" + player.getWorld().getName());
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
