package work.alsace.alsacecore.commands.builderTools;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class AdvanceFlyCommand implements CommandExecutor, Listener {
    private static final List<String> slower = new ArrayList();
    private static final List<String> slower2 = new ArrayList();
    public static Set<Player> enabledPlayers = new HashSet();
    private final HashMap<String, Double> lastVelocity = new HashMap();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission("alsace.commands.advfly")) {
            return false;
        }

        Player player = (Player) sender;

        if (enabledPlayers.contains(player)) {
            enabledPlayers.remove(player);
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.setGameMode(GameMode.CREATIVE);
            }
            player.sendMessage(ChatColor.GRAY + "已禁用进阶飞行模式");
        } else {
            enabledPlayers.add(player);
            player.sendMessage(ChatColor.GRAY + "已启用进阶飞行模式");
        }

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().isFlying()) {
            if (!enabledPlayers.contains(e.getPlayer())) {
                return;
            }
            Double speed = e.getFrom().clone().add(0.0, -e.getFrom().getY(), 0.0).distance(e.getTo().clone().add(0.0, -e.getTo().getY(), 0.0));
            if (Math.abs(e.getFrom().getYaw() - e.getTo().getYaw()) > 2.5) {
                return;
            }
            if (Math.abs(e.getFrom().getPitch() - e.getTo().getPitch()) > 2.5) {
                return;
            }
            if (lastVelocity.containsKey(e.getPlayer().getName())) {
                Double lastSpeed = lastVelocity.get(e.getPlayer().getName());
                if (speed * 1.3 < lastSpeed) {
                    if (slower.contains(e.getPlayer().getName())) {
                        if (slower2.contains(e.getPlayer().getName())) {
                            Vector v = e.getPlayer().getVelocity().clone();
                            v.setX(0);
                            v.setZ(0);
                            e.getPlayer().setVelocity(v);
                            lastVelocity.put(e.getPlayer().getName(), 0.0);
                            slower.remove(e.getPlayer().getName());
                            slower2.remove(e.getPlayer().getName());
                        } else {
                            slower2.add(e.getPlayer().getName());
                        }
                    } else {
                        slower.add(e.getPlayer().getName());
                    }
                } else if (speed > lastSpeed) {
                    lastVelocity.put(e.getPlayer().getName(), speed);
                    slower.remove(e.getPlayer().getName());
                    slower2.remove(e.getPlayer().getName());
                }
            } else {
                lastVelocity.put(e.getPlayer().getName(), speed);
                slower.remove(e.getPlayer().getName());
            }
        }
    }
}