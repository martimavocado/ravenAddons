package at.raven.ravenAddons.mixin.hooks

import at.raven.ravenAddons.event.render.ToolTipEvent
import at.raven.ravenAddons.utils.EventUtils.postAndCatch
import net.minecraft.item.ItemStack

class TooltipHook {
    fun getTooltip(item: ItemStack, tooltip: MutableList<String>): List<String> {
        val event = ToolTipEvent(item, tooltip)

        if (event.postAndCatch()) return emptyList<String>()
        return tooltip
    }
}