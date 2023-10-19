package com.example.androidwithjetpackcomposeconcepts.learning

// Broadcasts are the system wide events that your app can consume or receive
// this can be sent by android OS or by your own app, or by any other app
// Your app can register a broadcast receiver for such broadcasts so that it gets triggered when broadcasts events happen
// Inside activity we call registerReceiver() to register our broadcast receiver
// Dynamic broadcast receiver -> we declare in our activity ,so there we needed that
// Statically also we can set inside Manifest