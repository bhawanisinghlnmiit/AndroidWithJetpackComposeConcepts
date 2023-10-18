package com.example.androidwithjetpackcomposeconcepts.learning

// Intents are just like an envelope to pass the intention of your app from one component to other component
// To communicate between different components

// Android Manifest -> summarizes the functionality of our app to outside world like components, permissions

// Explicit and Implicit intents
//
// EXPLICIT INTENT -> we know what component or app we want to open
// //                        this will launch our own activity component
////                        Intent(this@MainActivity, SecondActivity::class.java).also {
////                            startActivity(it)
////                        }
//                        Intent(Intent.ACTION_MAIN).also {
//                            it.`package` = "com.example.newsinshort"
//                            try {
//                                startActivity(it)
//                            } catch (e : ActivityNotFoundException){
//                                e.printStackTrace()
//                            }
//                        }

// IMPLICIT INTENT -> Just specifying actions and then android will check which apps will do that action and android will show the chooser
// try {
//                            Intent(Intent.ACTION_SEND).apply {
//                                type = "text/plain"
//                                putExtra(Intent.EXTRA_EMAIL, arrayOf("test@gmail.com"))
//                                putExtra(Intent.EXTRA_SUBJECT, "This is my subject")
//                                putExtra(Intent.EXTRA_TEXT, "This is the content of my mail")
//                            }
//                        } catch (e: ActivityNotFoundException){
//                            e.printStackTrace()
//                            Log.d("Hello world", "No activity found")
//                        }


/// INTENT_FILTER
// What if we want to register our app to receive intents from other app