package work.alsace.alsacecore.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.TPAHandler;

public class TPACommand implements CommandExecutor {
    private final AlsaceCore plugin;
    private final TPAHandler tpaHandler;

    public TPACommand(AlsaceCore plugin) {
        this.plugin = plugin;
        this.tpaHandler = plugin.getTPAHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.tp")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return true;
        }
        if (label.equalsIgnoreCase("tpahere")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "正确指令:\n§f/tpahere <玩家> §7- 请求玩家传送到你的位置");
                return false;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "你不能向自己发送传送请求");
                return false;
            }
            if (target != null && target.isOnline()) {
                Player senderPlayer = (Player) sender;
                tpaHandler.sendTPARequest(senderPlayer, target);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "玩家 " + args[0] + " 不在线。");
                return false;
            }
        } else if (label.equalsIgnoreCase("tpaccept")) {
            tpaHandler.acceptTPA((Player) sender);
            sender.sendMessage(ChatColor.GRAY + "你接受了传送请求");
            return true;
        } else if (label.equalsIgnoreCase("tpdeny")) {
            tpaHandler.denyTPA((Player) sender);
            sender.sendMessage(ChatColor.GRAY + "你拒绝了传送请求");
            return true;
        }
        return false;
    }

}
