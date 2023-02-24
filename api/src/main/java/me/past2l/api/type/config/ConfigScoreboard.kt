package me.past2l.api.type.config

data class ConfigScoreboard(
    val title: String = "$(server.name)",
    val content: ArrayList<String> = arrayListOf(
        "&8$(date.year)/$(date.month)/$(date.day) $(date.hour):$(date.minute)&r",
        "",
        "name : &6$(player.name)&r",
        "op: &6$(player.op)&r",
    ),
)
