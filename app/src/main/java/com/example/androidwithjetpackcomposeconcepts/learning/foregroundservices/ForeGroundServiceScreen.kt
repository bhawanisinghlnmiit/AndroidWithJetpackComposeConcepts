package com.example.androidwithjetpackcomposeconcepts.learning.foregroundservices

import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ForeGroundServiceScreen(
    viewModel: ForegroundServiceViewModel
){
    val context = LocalContext.current
    val isServiceRunning by viewModel.serviceIsRunning.collectAsState()

    Button(onClick = {
        Intent(context, RunningService::class.java).also {
            it.action = RunningService.Actions.START.toString()
            context.startService(it)
        }
        viewModel.startService()
    }) {
        Text(text = "Start Foreground service")
    }

    Spacer(modifier = Modifier.size(20.dp))

    Button(onClick = {
        Intent(context, RunningService::class.java).also {
            it.action = RunningService.Actions.STOP.toString()
            context.stopService(it)
        }
        viewModel.stopService()
    }) {
        Text(text = "Stop Foreground service")
    }
    Spacer(modifier = Modifier.size(20.dp))

    if(isServiceRunning){
        Text(text = "Service is running....")
    } else {
        Text(text = "Service is stopped.....")
    }
}
