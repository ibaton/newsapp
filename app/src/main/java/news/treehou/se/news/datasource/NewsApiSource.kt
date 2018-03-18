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

    /**
     * Get news sources from news api.
     */
    fun getSources(): Flowable<List<NewsSource>> {
        return Flowable.combineLatest(
                database.sourceDao().all,
                requestAndCacheSources(),
                BiFunction { sources, _ -> sources }
        )
    }

    /**
     * Get watched news sources from news api.
     */
    fun getWatchedSources(): Flowable<List<NewsSource>> {
        return getSources().filterWatchedSources()
    }

    /**
     * Save data sources to database.
     */
    fun updateSources(vararg source: NewsSource) {
        database.sourceDao().updateAll(*source)
    }

    /**
     * Search for articles and return a stream object emitting an object when articles are
     * collected.
     *
     * @param searchText text to search for
     * @return flowable emitting list of articles matching search term
     */
    fun getArticles(searchText: String): Flowable<List<NewsArticle>> {
        return getSources().filterWatchedSources()
                .filter { it.isNotEmpty() }
                .switchMap { getNewsForSource(searchText, *it.toTypedArray()) }
                .onErrorReturn { listOf() }
    }

    /**
     * Get top headlines from watched sources.
     *
     * @return flowable emitting list of top headlines
     */
    fun getTopHeadlines(): Flowable<List<NewsArticle>> {
        return getSources().filterWatchedSources()
                .switchMap { getTopHeadlinesForSource(*it.toTypedArray()) }
                .onErrorReturn { listOf() }
    }

    private fun queryFormatSources(vararg source: NewsSource): String {
        return listOf(*source).joinToString(separator = ",") { it.id }
    }

    private fun getNewsForSource(searchText: String, vararg source: NewsSource): Flowable<List<NewsArticle>> {
        val queryFormattedSource = queryFormatSources(*source)
        return newsApi.everything(searchText, sources = queryFormattedSource).map { it.articles }
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
     *
     * @return flowable emitting watch news sources.
     */
    fun Flowable<List<NewsSource>>.filterWatchedSources(): Flowable<List<NewsSource>> {
        return this.map { it.filter { source -> source.watched } }
    }
}
