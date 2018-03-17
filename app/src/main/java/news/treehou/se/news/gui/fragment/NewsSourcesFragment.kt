package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_sources.*
import news.treehou.se.news.R
import news.treehou.se.news.datasource.NewsApiSource
import news.treehou.se.news.gui.adapter.NewsSourceAdapter
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class NewsSourcesFragment : BaseFragment(R.layout.fragment_news_sources) {

    @Inject lateinit var newsApi: NewsApiSource

    private val adapter = NewsSourceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sourceListView.adapter = adapter
        sourceListView.layoutManager = LinearLayoutManager(context)
        sourceListView.itemAnimator = DefaultItemAnimator()
    }

    override fun onResume() {
        super.onResume()
        val context = context

        if(context != null){
            newsApi.getSources()
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { sources -> sources.sortedWith(compareBy({!it.watched},{it.name})) }
                    .subscribe({
                        adapter.addSources(it)
                    })

            adapter.getCheckChangedFlowable()
                    .compose(bindToLifecycle())
                    .observeOn(Schedulers.io())
                    .subscribe({ (source, checked) ->
                        source.watched = checked
                        newsApi.updateSources(source)
                    })
        }
    }
}
