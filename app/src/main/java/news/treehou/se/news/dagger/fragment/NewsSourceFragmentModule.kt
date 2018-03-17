package news.treehou.se.news.dagger.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.gui.fragment.NewsSourcesFragment


@Module
internal abstract class NewsSourceFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeFragmentInjector(): NewsSourcesFragment
}
