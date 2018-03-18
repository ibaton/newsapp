package news.treehou.se.news.dagger

import dagger.Component
import dagger.android.AndroidInjector
import news.treehou.se.news.App
import news.treehou.se.news.dagger.activity.NewsBrowserActivityModule
import news.treehou.se.news.dagger.activity.MainActivityModule
import news.treehou.se.news.dagger.activity.NewsSourcesActivityModule
import news.treehou.se.news.dagger.fragment.NewsBrowserFragmentModule
import news.treehou.se.news.dagger.fragment.TopHeadlinesFragmentModule
import news.treehou.se.news.dagger.fragment.NewsSourceFragmentModule
import javax.inject.Singleton


@Component(modules = [
    AppModule::class,
    MainActivityModule::class,
    NewsSourcesActivityModule::class,
    NewsBrowserActivityModule::class,
    NewsBrowserFragmentModule::class,
    TopHeadlinesFragmentModule::class,
    NewsSourceFragmentModule::class
])
@Singleton
interface AppComponent: AndroidInjector<App> {
}