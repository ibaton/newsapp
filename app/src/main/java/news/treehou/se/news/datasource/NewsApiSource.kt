package news.treehou.se.news.datasource

import android.content.Context
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import news.treehou.se.news.database.NewsDatabase
import news.treehou.se.news.model.NewsArticle
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

    fun getArticles(): Flowable<List<NewsArticle>> {
        return getSources().filterWatchedSources()
                .filter { it.isNotEmpty() }
                .switchMap { getNewsForSource(*it.toTypedArray()) }
    }

    fun getTopHeadlines(): Flowable<List<NewsArticle>> {
        return getSources().filterWatchedSources()
                .filter { it.isNotEmpty() }
                .switchMap { getTopHeadlinesForSource(*it.toTypedArray()) }
    }

    private fun queryFormatSources(vararg source: NewsSource): String {
        return listOf(*source).joinToString(separator = ",") { it.id }
    }

    private fun getNewsForSource(vararg source: NewsSource): Flowable<List<NewsArticle>> {
        val queryFormattedSource = queryFormatSources(*source)
        return newsApi.everything(sources=queryFormattedSource).map { it.articles }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun getTopHeadlinesForSource(vararg source: NewsSource): Flowable<List<NewsArticle>> {
        val queryFormattedSource = queryFormatSources(*source)
        return newsApi.topHeadlines(sources=queryFormattedSource).map { it.articles }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    /**
     * Request sources from api and cache them in database.
     */
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
