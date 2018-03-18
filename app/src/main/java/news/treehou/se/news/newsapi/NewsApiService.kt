package news.treehou.se.news.newsapi

import io.reactivex.Observable
import news.treehou.se.news.newsapi.message.Message
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit api specification for NewsApi
 */
interface NewsApiService {
    @GET("sources")
    fun listSources(@Query("apiKey") apiKey: String = NEWS_API_KEY): Observable<Message.SourceMessage>

    @GET("everything")
    fun everything(@Query("sources") sources: String? = null, @Query("apiKey") apiKey: String = NEWS_API_KEY): Observable<Message.ArticleMessage>

    @GET("top-headlines")
    fun topHeadlines(@Query("sources") sources: String? = null, @Query("apiKey") apiKey: String = NEWS_API_KEY): Observable<Message.ArticleMessage>
}