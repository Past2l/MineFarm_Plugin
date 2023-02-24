package me.past2l.api.type.config

data class ConfigTabList(
    val playerName: String = "$(player.name)",
    val header: String = "$(server.name)",
    val footer: String = "players : $(server.players)",
)
