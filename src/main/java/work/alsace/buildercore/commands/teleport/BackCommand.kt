package work.alsace.buildercore.commands.teleport

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import java.util.*

class BackCommand(private val plugin: BuilderCore) : CommandExecutor {
    private val locationHistory = HashMap<UUID, Stack<Location>>()
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该命令只能由玩家使用")
            return false
        }
        val playerId: UUID = sender.uniqueId

        // 确保该玩家有自己的位置历史栈
        locationHistory.putIfAbsent(playerId, Stack())
        val playerLocationHistory = locationHistory[playerId]!! // 获取玩家的位置历史栈
        if (playerLocationHistory.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "没有位置记录")
            return false
        }
        var backSteps = 1 // 默认返回上一个位置
        if (args.isNotEmpty()) {
            try {
                backSteps = args[0].toInt()
                if (backSteps < 1 || backSteps > plugin.backHistory) {
                    sender.sendMessage(ChatColor.RED.toString() + "无效的参数，只能是1-5")
                    return false
                }
            } catch (e: NumberFormatException) {
                sender.sendMessage(ChatColor.RED.toString() + "无效的参数，只能是数字")
                return false
            }
        }
        if (playerLocationHistory.size < backSteps) {
            sender.sendMessage(ChatColor.RED.toString() + "没有足够的位置记录")
            return false
        }
        var targetLocation: Location? = null
        for (i in 0 until backSteps) {
            targetLocation = playerLocationHistory.pop()
        }
        if (targetLocation != null) {
            sender.teleport(targetLocation)
            sender.sendMessage(ChatColor.GRAY.toString() + "返回到之前的位置")
        }
        return true
    }

    fun addToHistory(player: Player) {
        val currentLocation = player.location.clone()
        val playerId = player.uniqueId
        locationHistory.putIfAbsent(playerId, Stack()) // 确保玩家有自己的栈
        val playerLocationHistory = locationHistory[playerId]!!
        playerLocationHistory.push(currentLocation)
        val maxHistorySize = plugin.backHistory
        while (playerLocationHistory.size > maxHistorySize) {
            playerLocationHistory.removeAt(0)
        }
    }
}
