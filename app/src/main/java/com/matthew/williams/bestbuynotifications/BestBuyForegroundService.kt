package com.matthew.williams.bestbuynotifications

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.matthew.williams.bestbuynotifications.ProductData.Companion.linkData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Exception


class BestBuyForegroundService : Service() {
    var isServiceRunning = false
    var updateController: UpdateController = UpdateController(this)
    val delaySecondsBetweenRequests = 10
    val activityDisplayData: MutableLiveData<ArrayList<String>> =
        MutableLiveData(ArrayList<String>(linkData.size))

    override fun onCreate() {
        super.onCreate()

        for (i in 0 until linkData.size) {
            activityDisplayData.value!!.add("init")
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (!isServiceRunning) {

            Log.i(this::class.simpleName, "Received Start Foreground Intent ")
            showNotification()
            Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show()
            for (i in 0 until linkData.size) {
                CoroutineScope(IO).launch {
                    try {
                        while (isActive) {
                            val response = updateController.doUpdate(i)
                            activityDisplayData.value?.set(i, response)
                            activityDisplayData.postValue(activityDisplayData.value)
                            delay((delaySecondsBetweenRequests * 1000).toLong())
                        }
                    } catch (e: Exception) {
                        Log.e(this::class.simpleName, "updater error", e)
                        activityDisplayData.value?.set(
                            i,
                            "update loop broken; error:\n" + e.message
                        )
                        activityDisplayData.postValue(activityDisplayData.value)

                    }
                }
            }
            isServiceRunning = true
        }
        return START_STICKY
    }

    private fun showNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0)

        val notification: Notification =
            Notification.Builder(this, BestBuyNotifications.GENERAL_NOTIFICATION_CHANNEL)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText("Checking every 10 seconds...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(MainActivity.ONGOING_NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this::class.simpleName, "In onDestroy")
        Toast.makeText(this, "Service Destroyed!", Toast.LENGTH_SHORT).show()
    }

    inner class BestBuyForegroundServiceBinder : Binder() {
        fun getService(): BestBuyForegroundService = this@BestBuyForegroundService
    }

    override fun onBind(intent: Intent): IBinder {
        return BestBuyForegroundServiceBinder()
    }

}