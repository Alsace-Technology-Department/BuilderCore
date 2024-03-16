package work.alsace.buildercore.commands.tools

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import work.alsace.buildercore.BuilderCore

class BuilderCoreCommand(private val plugin: BuilderCore) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, s: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("buildercore.admin")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.size == 1) {
            when (args[0]) {
                "info" -> sender.sendMessage(
                    """
    ${ChatColor.GRAY}插件名称: §fBuilderCore
    §7插件版本: §f${plugin.description.version}
    §7插件作者: §f
    """.trimIndent() + plugin.description.authors.toTypedArray().contentToString()
                )

                "reload" -> {
                    plugin.loadConfig()
                    sender.sendMessage(ChatColor.GRAY.toString() + "重载成功")
                }

                else -> sender.sendMessage(
                    """
    ${ChatColor.GRAY}正确指令:
    §f/buildercore info §7- 查看插件信息
    §f/buildercore reload §7- 重载插件
    """.trimIndent()
                )
            }
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/buildercore info §7- 查看插件信息
    §f/buildercore reload §7- 重载插件
    """.trimIndent()
            )
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, s: String, args: Array<String>): List<String> {
        return if (args.size == 1 && sender.hasPermission("buildercore.admin")) {
            commands
        } else ArrayList(0)
    }

    companion object {
        private val commands: List<String> = mutableListOf("reload", "info")
    }
}
