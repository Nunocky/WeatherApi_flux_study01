package org.nunocky.weatherapi_flux_study01.dispatcher

import org.greenrobot.eventbus.EventBus
import org.nunocky.weatherapi_flux_study01.action.Action
import org.nunocky.weatherapi_flux_study01.store.StoreChangeEvent

class Dispatcher {
    companion object {
        private var instance: Dispatcher? = null

        fun get(): Dispatcher {
            if (instance == null) {
                instance = Dispatcher()
            }
            return instance!!
        }
    }

    fun register(cls: Any?) {
        EventBus.getDefault().register(cls)
    }

    fun unregister(cls: Any?) {
        EventBus.getDefault().unregister(cls)
    }

    fun dispatch(type: String?, vararg data: Any?) {
        require(!isEmpty(type)) { "Type must not be empty" }
        require(data.size % 2 == 0) { "Data must be a valid list of key,value pairs" }
        val actionBuilder: Action.Builder = Action.type(type)
        var i = 0
        while (i < data.size) {
            val key = data[i++] as String?
            val value = data[i++]
            actionBuilder.bundle(key, value)
        }
        post(actionBuilder.build())
    }

    fun emitChange(event: StoreChangeEvent) {
        post(event)
    }

    private fun isEmpty(type: String?): Boolean {
        return type == null || type.isEmpty()
    }

    private fun post(event: Any) {
        EventBus.getDefault().post(event)
    }
}