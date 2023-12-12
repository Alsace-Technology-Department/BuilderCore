package work.alsace.alsacecore.commands.agree;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.DataBaseManager;

import java.util.UUID;

public class DisagreeCommand implements CommandExecutor {
    private final AlsaceCore plugin;
    private final DataBaseManager databaseManager;

    public DisagreeCommand(AlsaceCore plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getDatabaseManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            databaseManager.delPlayerAgreed(playerUUID, false);
            player.kickPlayer("您必须阅读并同意《阿尔萨斯工业园用户协议》才能使用工业园的服务！");
            return true;

        } else {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用这个命令！");
            return false;
        }
    }

}
