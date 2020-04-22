package org.nunocky.weatherapi_flux_study01.action

import org.nunocky.weatherapi_flux_study01.api.WeatherResponse

sealed class WeatherApiAction<out T>(override val type: String) : Action<T> {
//    companion object {
//        const val FETCH_START = "fetch_start"
//        const val FETCH_WEATHER = "fetch_weather"
//        const val NETWORK_ERROR = "network_error"
//    }

    class StartFetch(override val data: Unit) : WeatherApiAction<Unit>(TYPE) {
        companion object {
            const val TYPE = "fetch_start"
        }
    }

    class WeatherFetched(override val data: WeatherResponse) : WeatherApiAction<WeatherResponse>(TYPE) {
        companion object {
            const val TYPE = "fetch_weather"
        }
    }

    class NetworkError(override val data: Throwable) : WeatherApiAction<Throwable>(TYPE) {
        companion object {
            const val TYPE = "network_error"
        }

    }
}