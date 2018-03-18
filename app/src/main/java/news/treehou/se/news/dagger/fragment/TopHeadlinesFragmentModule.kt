package news.treehou.se.news.dagger.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.dagger.scope.FragmentScope
import news.treehou.se.news.gui.fragment.TopHeadlinesBrowserFragment

@Module
internal abstract class TopHeadlinesFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeFragmentInjector(): TopHeadlinesBrowserFragment
}
