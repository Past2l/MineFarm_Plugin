package me.past2l.api.type.config

import me.past2l.api.type.config.text.ConfigText

data class ConfigData(
    val serverName: String = "Test Server",
    val timezone: String = "Asia/Seoul",
    val consolePrefix: String = "&6[&a$(server.name)&6]&r",
    val chat: String = "$(player.name) > $(chat.message)",
    val enable: ConfigEnable = ConfigEnable(),
    val tabList: ConfigTabList = ConfigTabList(),
    val scoreboard: ConfigScoreboard = ConfigScoreboard(),
    val money: ConfigMoney = ConfigMoney(),
    val text: ConfigText = ConfigText(),
)
