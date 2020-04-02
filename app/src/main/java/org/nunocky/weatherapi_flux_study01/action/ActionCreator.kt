package org.nunocky.weatherapi_flux_study01.action

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.nunocky.weatherapi_flux_study01.api.IWeatherApi
import org.nunocky.weatherapi_flux_study01.dispatcher.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.coroutines.CoroutineContext


class ActionCreator(private val dispatcher: Dispatcher) : CoroutineScope {
    companion object {
        private var instance: ActionCreator? = null

        fun get(dispatcher: Dispatcher): ActionCreator {
            if (instance == null) {
                instance = ActionCreator(dispatcher)
            }

            return instance!!
        }
    }

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var weatherApi: IWeatherApi

    init {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("http://weather.livedoor.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit?.create(IWeatherApi::class.java).let {
            if (it != null) {
                weatherApi = it
            } else {
                throw RuntimeException("can't get IWeatherApi instance")
            }
        }
    }

    fun cancelJobs() {
        job.cancel()
    }

    fun fetchWeather(cityId: Int) {
        launch {

            dispatcher.dispatch(WeatherApiActions.FETCH_START)

            runCatching {
                weatherApi.getWhether("$cityId")
                //throw NetworkErrorException("test")
            }
                .onSuccess {
                    dispatcher.dispatch(WeatherApiActions.FETCH_WEATHER, "response", it)
                }
                .onFailure {
                    dispatcher.dispatch(WeatherApiActions.NETWORK_ERROR, "exception", it)
                }
        }
    }
}
