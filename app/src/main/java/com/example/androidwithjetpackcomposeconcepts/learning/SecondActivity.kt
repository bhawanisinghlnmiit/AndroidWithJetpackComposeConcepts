package com.example.androidwithjetpackcomposeconcepts.learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.androidwithjetpackcomposeconcepts.ui.theme.AndroidWIthJetpackComposeConceptsTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidWIthJetpackComposeConceptsTheme {
                Text(
                    text = "Second Activity"
                )
            }
        }
    }
}