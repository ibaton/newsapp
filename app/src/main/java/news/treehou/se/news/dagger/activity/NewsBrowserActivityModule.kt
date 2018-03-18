package news.treehou.se.news.dagger.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.dagger.scope.ActivityScope
import news.treehou.se.news.gui.activity.BrowseNewsActivity

@Module
internal abstract class NewsBrowserActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeActivityInjector(): BrowseNewsActivity
}
