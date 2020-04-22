package org.nunocky.weatherapi_flux_study01

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import org.nunocky.weatherapi_flux_study01.action.ActionCreator
import org.nunocky.weatherapi_flux_study01.dispatcher.Dispatcher
import org.nunocky.weatherapi_flux_study01.store.Store

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val dispatcher = Dispatcher.get()
    private val actionCreator: ActionCreator by viewModels { ActionCreator.Factory(application, dispatcher) }
    private val store: Store by viewModels()

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

        store.processing.observe(this, Observer {
            button.isEnabled = !it
        })

        store.title.observe(this, Observer {
            tvTitle.text = it
        })

        store.description.observe(this, Observer {
            tvDescription.text = it
        })

        store.imageUrl.observe(this, Observer {
            if (it.isNotEmpty()) {
                Picasso.get().load(it).into(imageView)
            }
        })
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
