package news.treehou.se.news.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import news.treehou.se.news.App
import news.treehou.se.news.database.NewsDatabase
import news.treehou.se.news.newsapi.NewsApi
import news.treehou.se.news.newsapi.NewsApiService
import javax.inject.Singleton

@Module
class AppModule(var app: App) {

    @Provides
    fun provideContext(): Context {
        return app
    }

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService {
        return NewsApi.createNewsApiService()
    }

    @Provides
    @Singleton
    fun provideNewsApiDatabase(context: Context): NewsDatabase {
        return NewsDatabase.buildDatabase(context)
    }
}
