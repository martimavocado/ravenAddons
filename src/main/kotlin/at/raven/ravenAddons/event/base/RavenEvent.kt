package at.raven.ravenAddons.event.base

import at.raven.ravenAddons.config.ConfigManager
import at.raven.ravenAddons.event.hypixel.HypixelJoinEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

abstract class RavenEvent : Event() {
    open fun post(): Boolean {
        try {
            if (!ConfigManager.configInitialized && this !is HypixelJoinEvent) return false

            MinecraftForge.EVENT_BUS.post(this)
            return false
        } catch (_: Throwable) {
            return false
        }
    }
}