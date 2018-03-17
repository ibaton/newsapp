package news.treehou.se.news.dagger.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.dagger.scope.ActivityScope
import news.treehou.se.news.gui.activity.NewsSourcesActivity

@Module
internal abstract class NewsSourcesActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeActivityInjector(): NewsSourcesActivity
}
