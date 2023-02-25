package me.past2l.api.gui

import me.past2l.api.nms.NMS
import me.past2l.api.util.Config
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TabList {
    companion object {
        fun setHeaderFooter() {
            if (!Config.enable.tabList) return
            Bukkit.getOnlinePlayers().forEach {
                NMS.setTabList(
                    it,
                    Config.format(Config.tabList.header, player = it),
                    Config.format(Config.tabList.footer, player = it),
                )
            }
        }

        fun setPlayerName() {
            if (!Config.enable.tabList) return
            Bukkit.getOnlinePlayers().forEach {
                it.playerListName = Config.format(Config.tabList.playerName, player = it)
            }
        }
    }
}