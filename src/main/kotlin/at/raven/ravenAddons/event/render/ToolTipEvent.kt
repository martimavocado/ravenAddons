package at.raven.ravenAddons.event.render

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ToolTipEvent(val item: ItemStack, var tooltip: MutableList<String>) : Event()