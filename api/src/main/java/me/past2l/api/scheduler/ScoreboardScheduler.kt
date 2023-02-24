package me.past2l.api.scheduler

import me.past2l.api.PluginManager
import me.past2l.api.gui.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ScoreboardScheduler {
    companion object {
        private var id: Int? = null

        fun init() {
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                PluginManager.plugin,
                { Bukkit.getOnlinePlayers().forEach { Scoreboard.set(it) } },
                0,
                600
            )
        }

        fun remove() {
            if (id != null)
                Bukkit.getScheduler().cancelTask(id!!)
        }
    }
}