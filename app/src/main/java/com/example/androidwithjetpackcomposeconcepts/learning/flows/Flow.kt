package com.example.androidwithjetpackcomposeconcepts.learning.flows

import android.service.autofill.UserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/*
* Flow is an asynchronous data stream (which generally comes from a task) that emits values to the collector
* and that gets completed with or without completion
*
* Components -> Flow builder , operator, collector
*
* StateFlow and SharedFlow
*
* Both StateFlow and SharedFlow are built on the concept of hot flow
* cold flow -> it generates response for one collector
* hot flow -> generated response for multiple collectors
*
* StateFlow emits the current value to its collector , needs initial value
* SharedFlow , initial value is not mandatory, can replay previous values, has buffer storage
*
* Zip operator combines result of two flows together via some function
* val flowOne = flowOf(1, 2, 3)
val flowTwo = flowOf("A", "B", "C")

flowOne.zip(flowTwo) { intValue, stringValue ->
    "$intValue$stringValue"
}.collect {
    println(it)
}
*
* The output will be the following:
1A
2B
3C
*
*
*
*
* */

interface NewsApi {
     fun fetchNews(): List<ArticleHeadline>
}
data class ArticleHeadline(
    val title: String
)

class NewsRemoteDataSource(
    private val newsApi: NewsApi,
    private val refreshIntervalMs: Long = 5000
) {
    val latestNews: Flow<List<ArticleHeadline>> = flow {
        while (true) {
            val latestNews = newsApi.fetchNews()
            emit(latestNews)
            delay(refreshIntervalMs)
        }
    }
}


//class NewsRepository(
//    private val newsRemoteDataSource: NewsRemoteDataSource,
//    private val userData: UserData,
//    private val defaultDispatcher: CoroutineDispatcher
//) {
//    val favoriteLatestNews: Flow<List<ArticleHeadline>> =
//        newsRemoteDataSource.latestNews
//            .map { news -> // Executes on the default dispatcher
//                news.filter { userData.isFavoriteTopic(it) }
//            }
//            .onEach { news -> // Executes on the default dispatcher
//                saveInCache(news)
//            }
//            // flowOn affects the upstream flow ↑
//            .flowOn(defaultDispatcher)
//            // the downstream flow ↓ is not affected
//            .catch { exception -> // Executes in the consumer's context
//                emit(lastCachedNews())
//            }
//}