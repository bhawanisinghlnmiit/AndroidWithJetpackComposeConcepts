package com.example.androidwithjetpackcomposeconcepts.learning.foregroundservices

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidwithjetpackcomposeconcepts.R

// we need to start this service from our activity by sending an intent to this service class
// with the start action defined, because that will call our start function
// we have to create a channel , and we typically do that when our app is launched, and we do it in Application class
class RunningService: Service() {
    // this service receives an intent and returns an IBinder object
    // this function is used to create a bound service
    // this function also does create one single instance and multiple components can connect with that single instance

    override fun onBind(p0: Intent?): IBinder? {
        return null // returning null means nothing can bind to this service
    }

    // this is called whenever any component send the intent to start this service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is Active")
            .setContentText("Elapsed time: 00:50")
            .build()

        startForeground(
             1,
            notification
        )
    }

    enum class Actions {
        START,
        STOP
    }
}