package org.nunocky.weatherapi_flux_study01.store

import android.util.Log
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

    private var weatherResponse: WeatherResponse? = null

    val weather: WeatherResponse?
        get() = weatherResponse

    private var processing: Boolean = false

    val isProcessing: Boolean
        get() = processing

    @Subscribe
    fun onEvent(event: Any) {
        if (event is Action) {

            when (event.type) {
                WeatherApiActions.FETCH_START -> {
                    processing = true
                    weatherResponse = null
                    emitStoreChange()
                }

                WeatherApiActions.FETCH_WEATHER -> {
                    processing = false
                    val hash = event.getData()
                    weatherResponse = hash?.get("response") as WeatherResponse
                    emitStoreChange()
                }

                WeatherApiActions.NETWORK_ERROR -> {
                    processing = false
                    weatherResponse = null;

                    val hash = event.getData()
                    val exception = hash?.get("exception") as Exception
                    Log.d(TAG, exception.toString())
                    emitStoreChange()
                }
            }
        }
    }

    private fun emitStoreChange() {
        dispatcher.emitChange(ApiStoreChangeEvent())
    }
}

