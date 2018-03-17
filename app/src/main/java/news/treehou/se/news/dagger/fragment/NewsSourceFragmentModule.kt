package news.treehou.se.news.dagger.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.dagger.scope.FragmentScope
import news.treehou.se.news.gui.fragment.NewsSourcesFragment


@Module
internal abstract class NewsSourceFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeFragmentInjector(): NewsSourcesFragment
}
