package news.treehou.se.news.dagger

import dagger.Component
import dagger.android.AndroidInjector
import news.treehou.se.news.App
import news.treehou.se.news.dagger.activity.MainActivityModule
import news.treehou.se.news.dagger.activity.NewsSourcesActivityModule
import news.treehou.se.news.dagger.fragment.NewsBrowserFragmentModule
import news.treehou.se.news.dagger.fragment.NewsSourceFragmentModule
import javax.inject.Singleton


@Component(modules = [
    AppModule::class,
    MainActivityModule::class,
    NewsSourcesActivityModule::class,
    NewsBrowserFragmentModule::class,
    NewsSourceFragmentModule::class
])
@Singleton
interface AppComponent: AndroidInjector<App> {
}