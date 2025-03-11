package at.raven.ravenAddons.features.misc

import at.raven.ravenAddons.event.CommandRegistrationEvent
import at.raven.ravenAddons.event.render.ToolTipEvent
import at.raven.ravenAddons.loadmodule.LoadModule
import at.raven.ravenAddons.utils.ChatUtils
import at.raven.ravenAddons.utils.InventoryUtils
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object ArmorDefenseHelper {
    @SubscribeEvent
    fun onTooltip(event: ToolTipEvent) {
        if (event.stack.item !is ItemArmor) return
        val item = event.stack.item as ItemArmor

        val armor = InventoryUtils.getPlayerArmor()
        val currentArmorReduction = getArmorStats(armor)

        armor[item.slotNumber()] = event.stack
        var difference = getArmorStats(armor).normalDamageReduction - currentArmorReduction.normalDamageReduction
        difference = (difference * 10).toInt() / 10f

        val diffString = when {
            difference > 0 -> "§a+$difference%"
            difference < 0 -> "§c$difference%"
            else -> return
        }

        event.tooltip.add(diffString)
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("armor") {
            description = "shows your current damage reduction"
            callback = {
                val armor = InventoryUtils.getPlayerArmor()
                var string = ""

                for ((index, item) in armor.withIndex()) {
                    string += item?.displayName + " $index |"
                }

                ChatUtils.chat(string)
            }
        }
    }


    private fun getArmorStats(inventory: Array<ItemStack?>): ArmorStats {
        var armorValue = 0
        var protectionValue = 0

        for (stack in inventory) {
            if (stack == null) continue
            val item = stack.item
            if (item !is ItemArmor) continue

            armorValue += item.damageReduceAmount

            if (stack.isItemEnchanted) {
                protectionValue += EnchantmentHelper.getEnchantmentLevel(0, stack)
            }
        }

        return ArmorStats(armorValue, protectionValue)
    }

    private fun ItemArmor.slotNumber(): Int {
        return listOf(3, 2, 1, 0)[this.armorType]
    }

    data class ArmorStats(
        val armor: Int,
        val protection: Int,
    ) {
        val normalDamageReduction: Float = run {
            val baseReduction = (armor * 4).coerceAtMost(80)
            val enchantmentReduction = (protection * 4).coerceAtMost(20)

            val finalReduction = baseReduction + (baseReduction * (enchantmentReduction/100f))

            finalReduction.coerceAtMost(100f)
        }
    }
}