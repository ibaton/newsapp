package news.treehou.se.news.datasource

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import news.treehou.se.news.database.NewsDatabase
import news.treehou.se.news.model.NewsArticle
import news.treehou.se.news.model.NewsSource
import news.treehou.se.news.newsapi.NewsApiService
import javax.inject.Inject

/**
 * Source of news articles.
 * Handles network requests and caching of data.
 */
class NewsApiSource @Inject constructor() {
    @Inject lateinit var database: NewsDatabase
    @Inject lateinit var newsApi: NewsApiService

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
                .onErrorReturn { listOf() }
    }

    fun getTopHeadlines(): Flowable<List<NewsArticle>> {
        return getSources().filterWatchedSources()
                .switchMap { getTopHeadlinesForSource(*it.toTypedArray()) }
                .onErrorReturn { listOf() }
    }

    private fun queryFormatSources(vararg source: NewsSource): String {
        return listOf(*source).joinToString(separator = ",") { it.id }
    }

    private fun getNewsForSource(vararg source: NewsSource): Flowable<List<NewsArticle>> {
        val queryFormattedSource = queryFormatSources(*source)
        return newsApi.everything(sources = queryFormattedSource).map { it.articles }
                .toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun getTopHeadlinesForSource(vararg source: NewsSource): Flowable<List<NewsArticle>> {
        val queryFormattedSource = queryFormatSources(*source)
        return newsApi.topHeadlines(sources = queryFormattedSource).map { it.articles }
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
                .onErrorReturn { listOf() }
    }

    /**
     * Filter out sources that isn't watched.
     */
    fun Flowable<List<NewsSource>>.filterWatchedSources(): Flowable<List<NewsSource>> {
        return this.map { it.filter { source -> source.watched } }
    }
}
