package news.treehou.se.news.newsapi

import io.reactivex.Observable
import news.treehou.se.news.newsapi.message.Message
import retrofit2.http.GET
import retrofit2.http.Query


val NewsApiKey = "cfd001f7ed5c40daa86862b4c1aa2ae1" // TODO Move to config file

/**
 * Retrofit api specification for NewsApi
 */
interface NewsApiService {
    @GET("sources")
    fun listSources(@Query("apiKey") apiKey: String = NewsApiKey): Observable<Message.SourceMessage>

    @GET("everything")
    fun everything(@Query("sources") sources: String? = null, @Query("apiKey") apiKey: String = NewsApiKey): Observable<Message.ArticleMessage>

    @GET("top-headlines")
    fun topHeadlines(@Query("sources") sources: String? = null, @Query("apiKey") apiKey: String = NewsApiKey): Observable<Message.ArticleMessage>
}