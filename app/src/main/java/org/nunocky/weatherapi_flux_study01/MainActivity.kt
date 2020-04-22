package org.nunocky.weatherapi_flux_study01

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import org.nunocky.weatherapi_flux_study01.action.ActionCreator
import org.nunocky.weatherapi_flux_study01.databinding.ActivityMainBinding
import org.nunocky.weatherapi_flux_study01.dispatcher.Dispatcher
import org.nunocky.weatherapi_flux_study01.store.Store

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val dispatcher = Dispatcher.get()
    private val actionCreator: ActionCreator by viewModels { ActionCreator.Factory(application, dispatcher) }
    private val store: Store by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            actionCreator.fetchWeather(400040)
        }

        store.imageUrl.observe(this, Observer {
            if (it.isNotEmpty()) {
                Picasso.get().load(it).into(binding.imageView)
            }
        })

        binding.lifecycleOwner = this
        binding.store = store
    }

    override fun onResume() {
        super.onResume()
        dispatcher.register(store)
    }

    override fun onPause() {
        super.onPause()
        dispatcher.unregister(store)
    }
}
