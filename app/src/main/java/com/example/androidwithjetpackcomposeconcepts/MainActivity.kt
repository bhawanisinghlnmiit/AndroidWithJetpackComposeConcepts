package com.example.androidwithjetpackcomposeconcepts

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.androidwithjetpackcomposeconcepts.learning.AirPlaneModeReciever
import com.example.androidwithjetpackcomposeconcepts.learning.fetchAndShowUser
import com.example.androidwithjetpackcomposeconcepts.learning.foregroundservices.ForeGroundServiceScreen
import com.example.androidwithjetpackcomposeconcepts.learning.foregroundservices.ForegroundServiceViewModel
import com.example.androidwithjetpackcomposeconcepts.learning.viewmodels.ImageViewModel
import com.example.androidwithjetpackcomposeconcepts.learning.workmanager.PhotoCompressionWorker
import com.example.androidwithjetpackcomposeconcepts.learning.workmanager.PhotoViewModel
import com.example.androidwithjetpackcomposeconcepts.ui.theme.AndroidWIthJetpackComposeConceptsTheme

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager

    private val photoViewModel by viewModels<PhotoViewModel>()

    private val foreGroundServiceViewModel by viewModels<ForegroundServiceViewModel>()

    private val imageViewModel by viewModels<ImageViewModel>()

    private val airPlaneModeReciever = AirPlaneModeReciever()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        fetchAndShowUser()
        Log.d("coroutines", "4")

        registerReceiver(
            airPlaneModeReciever,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )
        setContent {
            AndroidWIthJetpackComposeConceptsTheme {

                val workerResult = photoViewModel.workId?.let {id ->
                    workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                }

                LaunchedEffect(key1 = workerResult?.outputData){
                    if(workerResult?.outputData != null){
                        val filePath = workerResult.outputData.getString(
                            PhotoCompressionWorker.KEY_RESULT_PATH
                        )
                        filePath?.let {
                            val bitmap = BitmapFactory.decodeFile(
                                it
                            )
                            photoViewModel.updateCompressedBitmap(bitmap)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Log.d("my activity", imageViewModel.uri.toString())

                    imageViewModel.uri?.let {
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
                    Spacer(modifier = Modifier.size(20.dp))

                    ForeGroundServiceScreen(foreGroundServiceViewModel)
                    Spacer(modifier = Modifier.size(20.dp))

                    photoViewModel.uncompressedUri?.let {
                        Text(text = "Uncompressed photo:" )
                        AsyncImage(model = it, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    photoViewModel.compressedBitmap?.let {
                        Text(text = "Compressed photo:" )
                        Image(bitmap = it.asImageBitmap(), contentDescription = null)
                    }
                }
            }
        }
    }

    // this function only makes sense when our activity is SingleTop
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("MyActivity", "Received Intent: $intent")
        if (intent != null) {
            if (Intent.ACTION_SEND == intent.action && intent.type?.startsWith("image/") == true) { // Handle the received image intent
                val imageUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
                if (imageUri != null) { // You have received the image URI, you can perform actions with it.
                    // For example, display the image or process it as needed.
                }
                Log.d("MyActivity", imageUri.toString())
            }
        }

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        Log.d("MyActivity", uri.toString())
        Log.d("MyActivity", "Received Intent: $intent")
//        viewModel.uppdateUri(uri)

        photoViewModel.updateCompressUri(uri)
        // To launch our worker ,we need to define a work request
        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressionWorker.KEY_CONTENT_URI to uri.toString(),
                    PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
                )
            )
            .build()
        photoViewModel.updateWorkId(request.id)
        workManager.enqueue(request)


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneModeReciever)
    }
}
