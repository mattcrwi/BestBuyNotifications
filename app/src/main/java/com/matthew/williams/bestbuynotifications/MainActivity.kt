package com.matthew.williams.bestbuynotifications

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.matthew.williams.bestbuynotifications.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val ONGOING_NOTIFICATION_ID = 123
    }


    private lateinit var binding: ActivityMainBinding
    private var service: BestBuyForegroundService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder?) {
            service =
                (serviceBinder as BestBuyForegroundService.BestBuyForegroundServiceBinder).getService()
            service?.activityDisplayData?.observe(this@MainActivity, ::showData)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        Intent(this, BestBuyForegroundService::class.java).also {
            startService(it)
        }

        Intent(this, BestBuyForegroundService::class.java).also {
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun showData(data: ArrayList<String>) {
        var stringData = ""
        data.forEach {
            stringData += "$it\n\n"
        }
        binding.tvBody.text = stringData
    }
}