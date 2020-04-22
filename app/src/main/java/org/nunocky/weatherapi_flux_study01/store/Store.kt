package org.nunocky.weatherapi_flux_study01.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.nunocky.weatherapi_flux_study01.action.Action
import org.nunocky.weatherapi_flux_study01.action.WeatherApiActions
import org.nunocky.weatherapi_flux_study01.api.WeatherResponse

class Store : ViewModel() {
    companion object {
        private const val TAG = "Store"
    }

    private val _processing = MutableLiveData(false)
    val processing: LiveData<Boolean> = _processing

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _imageUrl = MutableLiveData("")
    val imageUrl: LiveData<String> = _imageUrl

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Any) {
        if (event is Action) {

            when (event.type) {
                WeatherApiActions.FETCH_START -> {
                    _processing.value = true

                    _title.value = ""
                    _description.value = ""
                    _imageUrl.value = ""
                }

                WeatherApiActions.FETCH_WEATHER -> {
                    val hash = event.getData()
                    val response = hash?.get("response") as WeatherResponse

                    _processing.value = false

                    _title.value = response.title
                    _description.value = response.description.text
                    _imageUrl.value = response.forecasts[0].image.url
                }

                WeatherApiActions.NETWORK_ERROR -> {
                    val hash = event.getData()
                    val exception = hash?.get("exception") as Exception

                    _processing.value = false

                    _title.value = "Network Error"
                    _description.value = ""
                    _imageUrl.value = ""
                }
            }
        }
    }
}

