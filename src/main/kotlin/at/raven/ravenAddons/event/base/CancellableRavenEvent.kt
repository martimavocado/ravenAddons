package at.raven.ravenAddons.event.base

abstract class CancellableRavenEvent : RavenEvent() {
    override fun post(): Boolean {
        super.post()
        return this.isCanceled
    }

    fun cancel() {
        this.isCanceled = true
    }

    override fun isCancelable(): Boolean = true
}