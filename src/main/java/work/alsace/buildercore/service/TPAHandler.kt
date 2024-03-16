package work.alsace.buildercore.service

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import java.util.*

class TPAHandler(plugin: BuilderCore?) {
    private val tpaRequests: MutableMap<String, String>

    init {
        tpaRequests = HashMap()
    }

    fun sendTPARequest(sender: Player, target: Player) {
        val targetName = target.name.lowercase(Locale.getDefault())
        val senderName = sender.name.lowercase(Locale.getDefault())
        tpaRequests[targetName] = senderName
        sender.sendMessage(ChatColor.GRAY.toString() + target.name + " 发送了传送请求。")
        target.sendMessage(ChatColor.GRAY.toString() + sender.name + " 想让你传送到他的位置。输入 /tpaccept 接受，或者 /tpdeny 拒绝。")
    }

    fun acceptTPA(player: Player) {
        val targetName = player.name.lowercase(Locale.getDefault())
        if (tpaRequests.containsKey(targetName)) {
            val senderName = tpaRequests[targetName]
            val sender = Bukkit.getPlayerExact(senderName!!)
            if (sender != null && sender.isOnline) {
                sender.sendMessage(ChatColor.GRAY.toString() + player.name + " 接受了你的传送请求。")
                player.teleport(sender.location)
            }
            tpaRequests.remove(targetName)
        } else {
            player.sendMessage(ChatColor.GRAY.toString() + "没有待处理的传送请求。")
        }
    }

    fun denyTPA(player: Player) {
        val targetName = player.name.lowercase(Locale.getDefault())
        if (tpaRequests.containsKey(targetName)) {
            val senderName = tpaRequests[targetName]
            val sender = Bukkit.getPlayerExact(senderName!!)
            if (sender != null && sender.isOnline) {
                sender.sendMessage(ChatColor.GRAY.toString() + player.name + " 拒绝了你的传送请求。")
            }
            tpaRequests.remove(targetName)
        } else {
            player.sendMessage(ChatColor.GRAY.toString() + "没有待处理的传送请求。")
        }
    }
}
