package org.nunocky.weatherapi_flux_study01.store

import org.greenrobot.eventbus.Subscribe
import org.nunocky.weatherapi_flux_study01.action.Action
import org.nunocky.weatherapi_flux_study01.action.WeatherApiActions
import org.nunocky.weatherapi_flux_study01.api.WeatherResponse
import org.nunocky.weatherapi_flux_study01.dispatcher.Dispatcher

class Store(private val dispatcher: Dispatcher) {
    companion object {
        private const val TAG = "Store"

        private var instance: Store? = null

        fun get(dispatcher: Dispatcher): Store {
            if (instance == null) {
                instance = Store(dispatcher)
            }
            return instance!!
        }
    }

    private var weatherResponse = WeatherResponse()
    private var processing: Boolean = false
    var networkException: Throwable? = null

    val response: WeatherResponse
        get() = weatherResponse

    val isProcessing: Boolean
        get() = processing

    val isError: Boolean
        get() = (networkException != null)

    @Subscribe
    fun onEvent(event: Any) {
        if (event is Action) {

            when (event.type) {
                WeatherApiActions.FETCH_START -> {
                    processing = true
                    weatherResponse = WeatherResponse()
                    networkException = null
                    emitStoreChange()
                }

                WeatherApiActions.FETCH_WEATHER -> {
                    processing = false
                    val hash = event.getData()
                    weatherResponse = hash?.get("response") as WeatherResponse
                    networkException = null
                    emitStoreChange()
                }

                WeatherApiActions.NETWORK_ERROR -> {
                    processing = false
                    weatherResponse = WeatherResponse()

                    val hash = event.getData()
                    val exception = hash?.get("exception") as Exception
                    networkException = exception
                    emitStoreChange()
                }
            }
        }
    }

    private fun emitStoreChange() {
        dispatcher.emitChange(ApiStoreChangeEvent())
    }
}

