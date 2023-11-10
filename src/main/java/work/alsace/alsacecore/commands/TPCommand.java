package work.alsace.alsacecore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;

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
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter((s) -> s.toLowerCase().startsWith(prefix)).collect(Collectors.toList());
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
                sender.sendMessage("§c参数不足，请补全参数");
                return true;
            }
            case 1 -> {
                if (sender instanceof Player commander) {
                    player = Bukkit.getPlayer(args[0]);
                    if (player == null) {
                        sender.sendMessage("§c玩家" + args[0] + "不在线");
                        return true;
                    }

                    String name = player.getName();
                    if (commander.getWorld().equals(player.getWorld())) {
                        if (!sender.hasPermission("alsace.command.tp.same") && !sender.hasPermission("chunanplugin.command.tp.across")) {
                            sender.sendMessage("§c你没有使用该命令的权限");
                            return true;
                        }

                        if (this.plugin.hasIgnored.get(name)) {
                            sender.sendMessage("§c玩家" + name + "已屏蔽强制传送");
                            return true;
                        }

                        commander.teleport(player);
                        commander.sendMessage("§a已传送至" + name);
                    } else if (commander.hasPermission("alsace.command.tp.across")) {
                        if (this.plugin.hasIgnored.get(name)) {
                            sender.sendMessage("§c玩家" + name + "已屏蔽强制传送");
                            return true;
                        }

                        commander.teleport(player);
                        commander.sendMessage("§a已传送至" + name);
                    } else {
                        commander.sendMessage("§c你没有使用该命令的权限");
                    }

                    return true;
                }
                sender.sendMessage("§c该指令仅限玩家执行");
                return true;
            }
            case 2 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.command.tp.location")) {
                        sender.sendMessage("§c你没有使用该命令的权限");
                        return true;
                    }
                    if (!sender.hasPermission("alsace.command.tp.other")) {
                        sender.sendMessage("§c你没有使用该命令的权限");
                        return true;
                    } else {
                        commander = Bukkit.getPlayer(args[0]);
                        player = Bukkit.getPlayer(args[1]);
                        if (commander == null) {
                            sender.sendMessage("§c玩家" + args[0] + "不在线");
                            return true;
                        } else {
                            if (player == null) {
                                sender.sendMessage("§c玩家" + args[1] + "不在线");
                                return true;
                            }

                            commander.teleport(player);
                            String var10001 = commander.getName();
                            sender.sendMessage("§a已将" + var10001 + "传送至" + player.getName());
                            return true;
                        }
                    }
                }
            }
            case 3 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.command.tp.location")) {
                        sender.sendMessage("§c你没有使用该命令的权限");
                        return true;
                    }

                    try {
                        x = this.getLocation(1, sender, args[0]);
                        y = this.getLocation(2, sender, args[1]);
                        z = this.getLocation(3, sender, args[2]);
                    } catch (Exception var14) {
                        commander.sendMessage("§c输入坐标有误，请重新输入");
                        return true;
                    }

                    loc = commander.getLocation();
                    commander.teleport(new Location(commander.getWorld(), x, y, z, loc.getYaw(), loc.getPitch()));
                    sender.sendMessage(String.format("§a已将%s传送至（%.2f, %.2f, %.2f）", commander.getName(), x, y, z));
                    return true;
                }
                sender.sendMessage("§c该指令仅限玩家执行");
                return true;
            }
            case 4 -> {
                if (sender instanceof Player commander) {
                    if (!sender.hasPermission("alsace.command.tp.location")) {
                        sender.sendMessage("§c你没有使用该命令的权限");
                        return true;
                    }
                    commander = args[0].equals("@s") ? (Player) sender : Bukkit.getPlayer(args[0]);
                    if (commander == null) {
                        if (!sender.hasPermission("alsace.command.tp.other")) {
                            sender.sendMessage("§c你没有使用该命令的权限");
                            return true;
                        }

                        sender.sendMessage("§c玩家" + args[0] + "不在线");
                        return true;
                    } else if (!sender.getName().equals(commander.getName()) && !sender.hasPermission("chunanplugin.command.tp.other")) {
                        sender.sendMessage("§c你没有使用该命令的权限");
                        return true;
                    } else {
                        try {
                            x = this.getLocation(1, sender, args[1]);
                            y = this.getLocation(2, sender, args[2]);
                            z = this.getLocation(3, sender, args[3]);
                        } catch (Exception var13) {
                            sender.sendMessage("§c输入坐标有误，请重新输入");
                            return true;
                        }

                        loc = commander.getLocation();
                        commander.teleport(new Location(commander.getWorld(), x, y, z, loc.getYaw(), loc.getPitch()));
                        sender.sendMessage(String.format("§a已将%s传送至（%.2f, %.2f, %.2f）", commander.getName(), x, y, z));
                        return true;
                    }
                }
            }
            default -> {
                sender.sendMessage("§c未知操作");
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
}
