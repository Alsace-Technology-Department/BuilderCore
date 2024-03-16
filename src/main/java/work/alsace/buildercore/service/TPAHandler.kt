package work.alsace.buildercore.service;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import work.alsace.buildercore.BuilderCore;

import java.util.HashMap;
import java.util.Map;

public class TPAHandler {
    private final Map<String, String> tpaRequests;

    public TPAHandler(BuilderCore plugin) {
        this.tpaRequests = new HashMap<>();
    }

    public void sendTPARequest(Player sender, Player target) {
        String targetName = target.getName().toLowerCase();
        String senderName = sender.getName().toLowerCase();

        tpaRequests.put(targetName, senderName);
        sender.sendMessage(ChatColor.GRAY + target.getName() + " 发送了传送请求。");
        target.sendMessage(ChatColor.GRAY + sender.getName() + " 想让你传送到他的位置。输入 /tpaccept 接受，或者 /tpdeny 拒绝。");
    }


    public void acceptTPA(Player player) {
        String targetName = player.getName().toLowerCase();

        if (tpaRequests.containsKey(targetName)) {
            String senderName = tpaRequests.get(targetName);
            Player sender = Bukkit.getPlayerExact(senderName);
            if (sender != null && sender.isOnline()) {
                sender.sendMessage(ChatColor.GRAY + player.getName() + " 接受了你的传送请求。");
                player.teleport(sender.getLocation());
            }
            tpaRequests.remove(targetName);
        } else {
            player.sendMessage(ChatColor.GRAY + "没有待处理的传送请求。");
        }
    }

    public void denyTPA(Player player) {
        String targetName = player.getName().toLowerCase();

        if (tpaRequests.containsKey(targetName)) {
            String senderName = tpaRequests.get(targetName);
            Player sender = Bukkit.getPlayerExact(senderName);
            if (sender != null && sender.isOnline()) {
                sender.sendMessage(ChatColor.GRAY + player.getName() + " 拒绝了你的传送请求。");
            }
            tpaRequests.remove(targetName);
        } else {
            player.sendMessage(ChatColor.GRAY + "没有待处理的传送请求。");
        }
    }
}
