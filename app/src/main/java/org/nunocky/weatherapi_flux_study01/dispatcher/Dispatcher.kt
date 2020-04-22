package org.nunocky.weatherapi_flux_study01.dispatcher

import org.greenrobot.eventbus.EventBus
import org.nunocky.weatherapi_flux_study01.action.WeatherApiAction
import org.nunocky.weatherapi_flux_study01.api.WeatherResponse

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

        when (type) {
            WeatherApiAction.FETCH_START -> {
                val action = WeatherApiAction.StartFetch(Unit)
                post(action)
            }

            WeatherApiAction.FETCH_WEATHER -> {
                val response = data[1] as WeatherResponse
                val action = WeatherApiAction.WeatherFetched(response)
                post(action)
            }

            WeatherApiAction.NETWORK_ERROR -> {
                val error = data[1] as Throwable
                val action = WeatherApiAction.NetworkError(error)
                post(action)
            }

            else -> {
                throw RuntimeException("unknown action type")
            }
        }
    }

    private fun isEmpty(type: String?): Boolean {
        return type == null || type.isEmpty()
    }

    private fun post(event: Any) {
        EventBus.getDefault().post(event)
    }
}