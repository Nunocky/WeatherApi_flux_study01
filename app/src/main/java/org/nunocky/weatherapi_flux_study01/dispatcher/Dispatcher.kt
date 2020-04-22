package org.nunocky.weatherapi_flux_study01.dispatcher

import org.greenrobot.eventbus.EventBus
import org.nunocky.weatherapi_flux_study01.action.WeatherApiAction

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

    fun <T> dispatch(action: WeatherApiAction<T>) {
        EventBus.getDefault().post(action)
    }

}