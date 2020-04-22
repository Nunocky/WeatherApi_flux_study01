package org.nunocky.weatherapi_flux_study01.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.nunocky.weatherapi_flux_study01.action.WeatherApiAction

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
    fun on(action: WeatherApiAction.StartFetch) {
        _processing.value = true

        _title.value = ""
        _description.value = ""
        _imageUrl.value = ""
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun on(action: WeatherApiAction.WeatherFetched) {

        val response = action.data

        _processing.value = false

        _title.value = response.title
        _description.value = response.description.text
        _imageUrl.value = response.forecasts[0].image.url
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun on(action: WeatherApiAction.NetworkError) {
        val exception = action.data

        _processing.value = false

        _title.value = "Network Error"
        _description.value = exception.message
        _imageUrl.value = ""
    }

}

