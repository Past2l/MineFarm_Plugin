package me.past2l.minefarm.util

import me.past2l.api.type.config.ConfigEnable
import me.past2l.api.type.config.ConfigScoreboard
import me.past2l.api.type.config.ConfigTabList
import me.past2l.api.type.entity.NPCData
import me.past2l.minefarm.type.gui.GUIShopItem
import me.past2l.minefarm.type.shop.ShopInteraction
import me.past2l.api.util.Config
import me.past2l.minefarm.type.config.ConfigData
import me.past2l.minefarm.type.config.ConfigMoney
import me.past2l.minefarm.type.config.text.ConfigText
import me.past2l.minefarm.type.config.text.ConfigTextShop
import org.bukkit.entity.Player
import java.text.DecimalFormat

class Config: Config() {
    companion object MineFarm {
        private lateinit var config: ConfigData

        lateinit var serverName: String
        lateinit var timezone: String
        lateinit var consolePrefix: String
        lateinit var chat: String
        lateinit var enable: ConfigEnable
        lateinit var tabList: ConfigTabList
        lateinit var scoreboard: ConfigScoreboard
        lateinit var text: ConfigText
        lateinit var money: ConfigMoney

        fun init() = API.init {
            val money = it?.get("money") as HashMap<*, *>?
            val text = it?.get("text") as HashMap<*, *>?
            val shopText = text?.get("shop") as HashMap<*, *>?
            val default = ConfigData()

            config = ConfigData(
                money = ConfigMoney(
                    money = money?.get("money")?.toString() ?: default.money.money,
                    cash = money?.get("cash")?.toString() ?: default.money.cash,
                ),
                text = ConfigText(
                    shop = ConfigTextShop(
                        item = shopText?.get("item")?.toString() ?: default.text.shop.item,
                        buyItemPrice = shopText?.get("buyItemPrice")?.toString() ?: default.text.shop.buyItemPrice,
                        buyItemLore = shopText?.get("buyItemLore")?.toString() ?: default.text.shop.buyItemLore,
                        buyAllItemLore = shopText?.get("buyAllItemLore")?.toString() ?: default.text.shop.buyAllItemLore,
                        sellItemPrice = shopText?.get("sellItemPrice")?.toString() ?: default.text.shop.sellItemPrice,
                        sellItemLore = shopText?.get("sellItemLore")?.toString() ?: default.text.shop.sellItemLore,
                        sellAllItemLore = shopText?.get("sellAllItemLore")?.toString() ?: default.text.shop.sellAllItemLore,
                    ),
                ),
            )

            this.serverName = API.config.serverName
            this.timezone = API.config.timezone
            this.consolePrefix = API.config.consolePrefix
            this.chat = API.config.chat
            this.enable = API.config.enable
            this.tabList = API.config.tabList
            this.scoreboard = API.config.scoreboard

            this.money = config.money
            this.text = config.text
        }

        fun save() = API.save point@{
            return@point hashMapOf(
                "money" to hashMapOf(
                    "money" to config.money.money,
                    "cash" to config.money.cash,
                ),
                "text" to hashMapOf(
                    "shop" to hashMapOf(
                        "item" to config.text.shop.item,
                        "buyItemPrice" to config.text.shop.buyItemPrice,
                        "buyItemLore" to config.text.shop.buyItemLore,
                        "buyAllItemLore" to config.text.shop.buyAllItemLore,
                        "sellItemPrice" to config.text.shop.sellItemPrice,
                        "sellItemLore" to config.text.shop.sellItemLore,
                        "sellAllItemLore" to config.text.shop.sellAllItemLore,
                    ),
                ),
            )
        }

        fun format(
            str: String?,
            player: Player? = null,
            npc: NPCData? = null,
            shopItem: GUIShopItem? = null,
            shopInteraction: ShopInteraction? = null,
        ) = API.format(str, player) point@{
            var result = it
            if (npc != null) {
                result = result.replace("%npc.id%", npc.id)
                    .replace("%npc.uuid%", npc.uuid.toString())
                    .replace("%npc.name%", npc.name)
            }
            if (shopItem != null) {
                result = result.replace("%shop.item.moneyType%", shopItem.moneyType)
                    .replace(
                        "%shop.item.buyPrice%",
                        DecimalFormat("#,###").format(shopItem.price ?: 0.0)
                    )
                    .replace(
                        "%shop.item.previousBuyPrice%",
                        DecimalFormat("#,###").format(shopItem.previousPrice ?: 0.0)
                    )
                    .replace(
                        "%shop.item.sellPrice%",
                        DecimalFormat("#,###").format(shopItem.sellPrice ?: 0.0)
                    )
                    .replace(
                        "%shop.item.previousSellPrice%",
                        DecimalFormat("#,###").format(shopItem.previousSellPrice ?: 0.0)
                    )
                    .replace(
                        "%shop.item.gap.buyPrice%",
                        if (!shopItem.priceChange) ""
                        else {
                            val gap = (shopItem.price ?: 0.0) - (shopItem.previousPrice ?: shopItem.price ?: 0.0)
                            if (gap < 1 && gap > -1) "(-)"
                            else if (gap > 0)
                                "&r&f(&c▲${DecimalFormat("#,###").format(gap)}&f)"
                            else
                                "&r&f(&9▼${DecimalFormat("#,###").format(-gap)}&f)"
                        }
                    )
                    .replace(
                        "%shop.item.gap.sellPrice%",
                        if (!shopItem.priceChange) ""
                        else {
                            val gap = (shopItem.sellPrice ?: 0.0) - (shopItem.previousSellPrice ?: shopItem.sellPrice ?: 0.0)
                            if (gap < 1 && gap > -1) "(-)"
                            else if (gap > 0)
                                "(&c▲${DecimalFormat("#,###").format(gap)}&f)"
                            else
                                "(&9▼${DecimalFormat("#,###").format(-gap)}&f)"
                        }
                    )
                if (shopInteraction != null) {
                    result = result.replace(
                        "%shop.item.name%",
                        if (shopInteraction.name.isNotEmpty()) shopInteraction.name + "&r"
                        else ""
                    )
                        .replace("%shop.item.type%", shopInteraction.type)
                        .replace("%shop.item.amount%", shopInteraction.amount.toString())
                        .replace(
                            "%shop.item.price%",
                            DecimalFormat("#,###").format(
                                when (shopInteraction.type) {
                                    "구매" -> (shopItem.price ?: 0.0) * shopInteraction.amount
                                    "판매" -> (shopItem.sellPrice ?: 0.0) * shopInteraction.amount
                                    else -> ""
                                }
                            )
                        )
                }
            }
            return@point result.replace("%money%", config.money.money)
                .replace("%cash%", config.money.cash)
        }
    }
}