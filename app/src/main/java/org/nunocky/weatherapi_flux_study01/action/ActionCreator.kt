package org.nunocky.weatherapi_flux_study01.action

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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


// MEMO : このサンプルでは ActionCreatorは AndroidViewModelのサブクラスである必要はない (ViewModelで十分)が、今後の実装のサンプルとしてコードを残しておく。

class ActionCreator(application: Application, private val dispatcher: Dispatcher) : AndroidViewModel(application), CoroutineScope {

    class Factory(private val application: Application, private val dispatcher: Dispatcher) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ActionCreator(application, dispatcher) as T
        }
    }

    companion object {
        private const val TAG = "ActionCreator"
    }

    // コルーチンの実行コンテキスト
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun fetchWeather(cityId: Int) {
        launch {
            dispatcher.dispatch(WeatherApiAction.FETCH_START)

            //delay(3000)

            runCatching {
                weatherApi.getWhether("$cityId")
                //throw NetworkErrorException("test")
            }
                .onSuccess {
                    dispatcher.dispatch(WeatherApiAction.FETCH_WEATHER, "response", it)
                }
                .onFailure {
                    dispatcher.dispatch(WeatherApiAction.NETWORK_ERROR, "exception", it)
                }
        }
    }
}
