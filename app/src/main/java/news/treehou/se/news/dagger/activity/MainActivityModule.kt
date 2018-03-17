package news.treehou.se.news.dagger.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.gui.activity.MainActivity


@Module
internal abstract class MainActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeActivityInjector(): MainActivity
}
