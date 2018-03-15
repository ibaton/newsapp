package news.treehou.se.news.datasource

import android.content.Context
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import news.treehou.se.news.database.NewsDatabase
import news.treehou.se.news.model.NewsSource
import news.treehou.se.news.newsapi.NewsApi

class NewsApiSource(val context: Context) {

    val database: NewsDatabase = NewsDatabase.getInstance(context)
    val newsApi = NewsApi.service

    fun getSources(): Flowable<List<NewsSource>> {
        return Flowable.combineLatest(
                database.sourceDao().all,
                requestAndCacheSources(),
                BiFunction { sources, _ -> sources }
        )
    }

    fun updateSources(vararg source: NewsSource) {
        database.sourceDao().updateAll(*source)
    }

    private fun requestAndCacheSources(): Flowable<List<NewsSource>> {
        return newsApi.listSources()
                .subscribeOn(Schedulers.io())
                .map { it.sources }
                .map {
                    database.sourceDao().insertAll(*it.toTypedArray())
                    it
                }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    companion object {

        @Volatile
        private var INSTANCE: NewsApiSource? = null

        fun getInstance(context: Context): NewsApiSource =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: NewsApiSource(context.applicationContext).also { INSTANCE = it }
                }

    }
}
