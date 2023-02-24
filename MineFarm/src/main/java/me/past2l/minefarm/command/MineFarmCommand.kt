package me.past2l.minefarm.command

import me.past2l.api.PluginManager
import me.past2l.api.util.Config
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class MineFarmCommand: CommandExecutor, TabExecutor {
    companion object {
        const val name = "minefarm"
    }

    private val help = hashMapOf(
        "default" to "§e---------------§r Help: /$name §e---------------§r\n" +
            "§6/$name reload§r: 마인팜 플러그인을 리로드합니다.",
    )

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (sender !is Player) return mutableListOf()
        return when (args.size) {
            1 -> mutableListOf("reload")
            else -> mutableListOf()
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        else if (args.isEmpty()) sender.sendMessage(help["default"])
        else when (args[0]) {
            "reload" -> {
                PluginManager.reload()
                sender.sendMessage(Config.format("&a플러그인이 리로드되었습니다.&r"))
            }
            else -> sender.sendMessage(help["default"])
        }
        return true
    }
}