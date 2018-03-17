package news.treehou.se.news.newsapi

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Class handling the connection to news api.
 */
class NewsApi {
    companion object {
        fun createNewsApiService(): NewsApiService{
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }
    }
}