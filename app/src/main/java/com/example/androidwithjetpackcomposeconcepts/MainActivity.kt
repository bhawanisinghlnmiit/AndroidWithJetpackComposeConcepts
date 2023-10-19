package com.example.androidwithjetpackcomposeconcepts

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.androidwithjetpackcomposeconcepts.learning.AirPlaneModeReciever
import com.example.androidwithjetpackcomposeconcepts.learning.viewmodels.ImageViewModel
import com.example.androidwithjetpackcomposeconcepts.ui.theme.AndroidWIthJetpackComposeConceptsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageViewModel>()
    private val airPlaneModeReciever = AirPlaneModeReciever()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(
            airPlaneModeReciever,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )
        setContent {
            AndroidWIthJetpackComposeConceptsTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Log.d("my activity", viewModel.uri.toString())
                    viewModel.uri?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                        )
                    }
                    Button(onClick = {
                        try {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("test@gmail.com"))
                                putExtra(Intent.EXTRA_SUBJECT, "This is my subject")
                                putExtra(Intent.EXTRA_TEXT, "This is the content of my mail")
                            }
                        } catch (e: ActivityNotFoundException){
                            e.printStackTrace()
                            Log.d("Hello world", "No activity found")
                        }
                    }) {
                        Text(text = "Click me")
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        Log.d("my activity", uri.toString())
        viewModel.uppdateUri(uri)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReciever)
    }
}
