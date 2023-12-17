package work.alsace.alsacecore.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import work.alsace.alsacecore.AlsaceCore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TPCommand implements TabExecutor {
    private final AlsaceCore plugin;

    public TPCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return new ArrayList<>(0);
        } else {
            String prefix = args[0].toLowerCase();
            return getAccessPlayers((Player) sender).stream().map(Player::getName).
                    filter((s) -> s.toLowerCase().startsWith(prefix))
                    .collect(Collectors.toList());
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        double x;
        double y;
        double z;
        Location loc;
        Player player;
        switch (args.length) {
            case 0 -> {
                sender.sendMessage(ChatColor.RED + "参数不足，请补全参数");
                return true;
            }
            //tp到玩家
            case 1 -> {
                if (sender instanceof Player commander) {
                    player = Bukkit.getPlayer(args[0]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "玩家" + args[0] + "不在线");
                        return true;
                    }

                    String name = player.getName();
                    if (commander.getWorld().equals(player.getWorld())) {
                        if (!sender.hasPermission("alsace.commands.tp")) {
                            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                            return true;
                        }

                        if (this.plugin.hasIgnored.get(name) != null && this.plugin.hasIgnored.get(name)) {
                            sender.sendMessage(ChatColor.RED + "玩家" + name + "已屏蔽强制传送");
                            return true;
                        }

                        teleportAfterDelay(commander, player);
                        commander.sendMessage(ChatColor.GRAY + "正在传送至" + name);
                    } else if (commander.hasPermission("alsace.commands.tp") && commander.hasPermission("multiverse.access." + player.getWorld().getName())) {
                        if (this.plugin.hasIgnored.get(name) != null && this.plugin.hasIgnored.get(name)) {
                            sender.sendMessage(ChatColor.RED + "玩家" + name + "已屏蔽强制传送");
                            return true;
                        }

                        teleportAfterDelay(commander, player);
                        commander.sendMessage(ChatColor.GRAY + "正在传送至" + name);
                    } else {
                        commander.sendMessage(ChatColor.RED + "你没有使用该命令或传送到目标世界权限");
                    }

                    return true;
                }
                sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
                return true;
            }
            //tp玩家到坐标
            case 2 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.commands.tp.location")) {
                        sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return true;
                    }
                    if (!sender.hasPermission("alsace.commands.tp.other")) {
                        sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                    } else {
                        commander = Bukkit.getPlayer(args[0]);
                        player = Bukkit.getPlayer(args[1]);
                        if (commander == null) {
                            sender.sendMessage(ChatColor.RED + "玩家" + args[0] + "不在线");
                        } else {
                            if (player == null) {
                                sender.sendMessage(ChatColor.RED + "玩家" + args[1] + "不在线");
                                return true;
                            }

                            teleportAfterDelay(commander, player);
                            String var10001 = commander.getName();
                            sender.sendMessage(ChatColor.GRAY + "已将" + var10001 + "传送至" + player.getName());
                        }
                    }
                    return true;
                }
            }
            //tp到坐标
            case 3 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.commands.tp.location")) {
                        sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return true;
                    }

                    try {
                        x = this.getLocation(1, sender, args[0]);
                        y = this.getLocation(2, sender, args[1]);
                        z = this.getLocation(3, sender, args[2]);
                    } catch (Exception var14) {
                        commander.sendMessage(ChatColor.RED + "输入坐标有误，请重新输入");
                        return true;
                    }

                    loc = commander.getLocation();
                    commander.teleport(new Location(commander.getWorld(), x, y, z, loc.getYaw(), loc.getPitch()));
                    sender.sendMessage(String.format(ChatColor.GRAY + "已将%s传送至（%.2f, %.2f, %.2f）", commander.getName(), x, y, z));
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
                return true;
            }
            //tp玩家到坐标
            case 4 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.commands.tp.location")) {
                        sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return true;
                    }
                    commander = args[0].equals("@s") ? (Player) sender : Bukkit.getPlayer(args[0]);
                    if (commander == null) {
                        if (!sender.hasPermission("alsace.commands.tp.other")) {
                            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                            return true;
                        }

                        sender.sendMessage(ChatColor.RED + "玩家" + args[0] + "不在线");
                        return true;
                    } else if (!sender.getName().equals(commander.getName()) && !sender.hasPermission("alsace.commands.tp.other")) {
                        sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return true;
                    } else {
                        try {
                            x = this.getLocation(1, sender, args[1]);
                            y = this.getLocation(2, sender, args[2]);
                            z = this.getLocation(3, sender, args[3]);
                        } catch (Exception var13) {
                            sender.sendMessage(ChatColor.RED + "输入坐标有误，请重新输入");
                            return true;
                        }

                        loc = commander.getLocation();
                        commander.teleport(new Location(commander.getWorld(), x, y, z, loc.getYaw(), loc.getPitch()));
                        sender.sendMessage(String.format(ChatColor.GRAY + "已将%s传送至（%.2f, %.2f, %.2f）", commander.getName(), x, y, z));
                        return true;
                    }
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "未知操作");
                return true;
            }
        }
        return false;
    }

    private double getLocation(int pos, CommandSender sender, String str) {
        double result;
        if (str.startsWith("~")) {
            if (!(sender instanceof Player)) {
                throw new IllegalArgumentException("The command sender is not a player, but the location contains '~'");
            }

            if (pos == 1) {
                result = ((Player) sender).getLocation().getX();
            } else if (pos == 2) {
                result = ((Player) sender).getLocation().getY();
            } else {
                result = ((Player) sender).getLocation().getZ();
            }

            if (str.length() > 1) {
                result += Double.parseDouble(str.substring(1, str.length() - 1));
            } else {
                result = Math.floor(result) + 0.5;
            }
        } else {
            result = Double.parseDouble(str);
        }

        return result;
    }

    private List<Player> getAccessPlayers(Player sender) {
        List<Player> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            player.getWorld();
            if (sender.hasPermission("multiverse.access." + player.getWorld().getName()))
                players.add(player);
        });
        return players;
    }

    private void teleportAfterDelay(Player fromPlayer, Player toPlayer) {
        toPlayer.sendMessage(fromPlayer.getName() + ChatColor.GRAY + " 正在传送到你身边，请稍候...");
        new BukkitRunnable() {
            @Override
            public void run() {
                fromPlayer.teleport(toPlayer);
                fromPlayer.sendMessage(ChatColor.GRAY + "你已成功传送到 " + toPlayer.getName() + " 身边！");
            }
        }.runTaskLater(plugin, 20L); // 1s
    }
}
