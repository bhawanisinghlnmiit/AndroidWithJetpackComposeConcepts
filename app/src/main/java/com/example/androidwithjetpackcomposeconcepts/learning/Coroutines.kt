package com.example.androidwithjetpackcomposeconcepts.learning

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Coroutines are like lightweight threads, Like threads coroutines can run in parallel, wait for each other and communicate
// Coroutines is framework built on top of actual Threading

// Dispatchers help coroutines decide on which thread the work has to be done
// IO -> disk , network
// MAIN -> UI related
// DEFAULT -> CPU intensive work
// Dispatchers.Unconfined when we do not care where the coroutine will be executed

// Here we have declared three functions inside our coroutine builder (launch) which will be called from main thread
// First doSomething() is called and it runs on the main thread
// Second doLongRunningTask() is called then it runs on the Background thread as we have changed the context
// in between UI thread is not blocked and free to do different tasks
// Third after doLongRunningTask() function is completed doSomeThingElse() is called


// launch() is used to start a coroutine but it does not returns a result, it returns a Job which is used to manage and control the coroutine,
// launch() is typically used to update UI , do some background work

// async is used to start a coroutine that computes a result , and you want to await a result or combine the result of multiple coroutines


// withContext() doesn't create a new coroutine-> it helps in shifting the context

//The major difference is that a coroutineScope will cancel whenever any of its children fail.
// If we want to continue with the other tasks even when one fails, we go with the supervisorScope.
// A supervisorScope won't cancel other children when one of them fails.

/*

Use coroutineScope with the top-level try-catch, when you do NOT want to continue with other tasks if any of them have failed.
If we want to continue with the other tasks even when one fails, we go with the supervisorScope.
Use supervisorScope with the individual try-catch for each task, when you want to continue with other tasks if one or some of them have failed.

*/

val job = CoroutineScope(Dispatchers.IO).launch {
    // Perform some work
}


val deferred = CoroutineScope(Dispatchers.IO).async {
    return@async fetchUser()
}
suspend fun tempFunForAwait(){
    val result = deferred.await()
}

fun fetchAndShowUser() {
    GlobalScope.launch(Dispatchers.Main) {
//        val user = fetchUser() // fetch on IO thread
//        showUser(user) // back on UI thread
//
        doSomething()
        doLongRunningTask()
        doSomethingElse()
    }
}

fun doSomething() {
    Log.d("coroutines", "1")
}

suspend fun doLongRunningTask() {
    withContext(Dispatchers.Default) {
        Log.d("coroutines", "2")
        delay(2000)
    }
}

fun doSomethingElse() {
    Log.d("coroutines", "3")
}

fun showUser(user: String) {
    //  show user
}

suspend fun fetchUser() : String {
   withContext(Dispatchers.IO){
       // make network call on IO thread
       // return user
   }
    return ""
}


