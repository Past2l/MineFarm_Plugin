package me.past2l.minefarm

import me.past2l.api.PluginManager
import me.past2l.minefarm.entity.CustomNPC
import me.past2l.api.entity.NPC
import me.past2l.api.entity.Player
import me.past2l.api.event.GUIEvent
import me.past2l.api.event.NPCEvent
import me.past2l.api.event.PacketEvent
import me.past2l.api.event.PlayerEvent
import me.past2l.api.gui.Scoreboard
import me.past2l.api.gui.TabList
import me.past2l.api.nms.NMS
import me.past2l.api.packet.Packet
import me.past2l.api.scheduler.NPCSkinReloadScheduler
import me.past2l.api.scheduler.ScoreboardScheduler
import me.past2l.minefarm.util.Config
import me.past2l.minefarm.command.CustomGUICommand
import me.past2l.minefarm.command.CustomNPCCommand
import me.past2l.minefarm.command.EnderChestGUICommand
import me.past2l.minefarm.gui.CustomGUI
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.plugin.java.JavaPlugin

class MineFarm: JavaPlugin() {
    override fun onEnable() {
        PluginManager.init(this)
        Config.init()
        Config.save()
        if (!NMS.init()) return
        Player.loadData()
        Player.saveData()
        this.initGUIs()
        this.initNPCs()
        this.initEvents()
        this.initCommands()
        this.initGameRules()
        this.initSchedulers()
    }

    override fun onDisable() {
        this.closeGUIs()
        this.removeNPCs()
        this.removeEvents()
        this.removeSchedulers()
    }

    private fun initCommands() {
        getCommand(CustomNPCCommand.name)?.executor = CustomNPCCommand()
        getCommand(CustomNPCCommand.name)?.tabCompleter = CustomNPCCommand()
        getCommand(CustomGUICommand.name)?.executor = CustomGUICommand()
        getCommand(CustomGUICommand.name)?.tabCompleter = CustomGUICommand()
        getCommand(EnderChestGUICommand.name)?.executor = EnderChestGUICommand()
        getCommand(EnderChestGUICommand.name)?.tabCompleter = EnderChestGUICommand()
    }

    private fun initEvents() {
        Packet.setEvent { player, packet ->
            CustomNPC.onInteractNPC(player, packet)
        }
        Packet.init()
        server.pluginManager.registerEvents(NPCEvent(), this)
        server.pluginManager.registerEvents(GUIEvent(), this)
        server.pluginManager.registerEvents(PlayerEvent(), this)
        server.pluginManager.registerEvents(PacketEvent(), this)
    }

    private fun removeEvents() {
        Packet.remove()
    }

    private fun initGUIs() {
        TabList.setHeaderFooter()
        TabList.setPlayerName()
        Bukkit.getOnlinePlayers().forEach { Scoreboard.set(it) }
        CustomGUI.init()
    }

    private fun initGameRules() {
        Bukkit.getWorlds().forEach {
            it.difficulty = Difficulty.NORMAL
            it.setGameRuleValue("announceAdvancements", "false")
            it.setGameRuleValue("keepInventory", "true")
            it.setGameRuleValue("commandBlockOutPut", "false")
            it.setGameRuleValue("doFireTick", "false")
            it.setGameRuleValue("doMobSpawning", "false")
            it.setGameRuleValue("spawnRadius", "0")
            it.setGameRuleValue("disableRaids", "false")
            it.setGameRuleValue("doInsomnia", "false")
        }
    }

    private fun initSchedulers() {
        ScoreboardScheduler.init()
        NPCSkinReloadScheduler.init()
    }

    private fun removeSchedulers() {
        ScoreboardScheduler.remove()
        NPCSkinReloadScheduler.remove()
    }

    private fun initNPCs() {
        CustomNPC.init()
    }

    private fun removeNPCs() {
        CustomNPC.remove()
        NPC.remove()
    }

    private fun closeGUIs() {
        Bukkit.getOnlinePlayers().forEach { Player.gui[it.uniqueId]?.close(it) }
    }
}