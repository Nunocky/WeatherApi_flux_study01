package org.nunocky.weatherapi_flux_study01

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.Subscribe
import org.nunocky.weatherapi_flux_study01.action.ActionCreator
import org.nunocky.weatherapi_flux_study01.dispatcher.Dispatcher
import org.nunocky.weatherapi_flux_study01.store.Store
import org.nunocky.weatherapi_flux_study01.store.StoreChangeEvent

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val dispatcher = Dispatcher.get()
    private val actionCreator = ActionCreator.get(dispatcher)
    private val store = Store.get(dispatcher)

    private lateinit var button: Button
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        imageView = findViewById(R.id.imageView)

        button.setOnClickListener {
            actionCreator.fetchWeather(400040)
        }
    }

    override fun onResume() {
        super.onResume()

        dispatcher.register(this)
        dispatcher.register(store)
    }

    override fun onPause() {
        super.onPause()
        dispatcher.unregister(this)
        dispatcher.unregister(store)
    }

    override fun onDestroy() {
        actionCreator.cancelJobs()
        super.onDestroy()
    }

    private fun updateUI() {
        if (store.isProcessing) {
            button.isEnabled = false
            tvTitle.text = ""
            tvDescription.text = ""
            imageView.setImageBitmap(null)
        } else {
            button.isEnabled = true

            store.weather?.let {
                tvTitle.text = it.title
                tvDescription.text = it.description?.text

                it.forecasts?.let { forecasts ->
                    forecasts[0].image?.url?.let { url ->
                        Picasso.get().load(url).into(imageView)
                    }
                }
            }
        }
    }

    @Subscribe
    fun onEvent(event: StoreChangeEvent) {
        updateUI()
    }
}