package news.treehou.se.news.dagger.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import news.treehou.se.news.gui.fragment.NewsBrowserFragment

@Module
internal abstract class NewsBrowserFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeFragmentInjector(): NewsBrowserFragment
}
