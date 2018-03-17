package news.treehou.se.news.dagger.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.dagger.scope.FragmentScope
import news.treehou.se.news.gui.fragment.NewsBrowserFragment

@Module
internal abstract class NewsBrowserFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeFragmentInjector(): NewsBrowserFragment
}
