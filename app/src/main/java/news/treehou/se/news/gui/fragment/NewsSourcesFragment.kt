package news.treehou.se.news.gui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sourceListView.adapter = adapter
        sourceListView.layoutManager = LinearLayoutManager(context)
        sourceListView.itemAnimator = DefaultItemAnimator()
    }

    override fun onResume() {
        super.onResume()
        val context = context

        if (context != null) {
            createRefreshFlowable()
                    .switchMap({ newsApi.getSources() })
                    .compose(bindToLifecycle())
                    .map { sources -> sources.sortedWith(compareBy({ !it.watched }, { it.name })) }
                    .map { it.map { NewsSourceAdapter.DataAdapterItem(it) } }
                    .addSourceWatchLabels(context)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefresh.isRefreshing = false
                        errorText.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                        adapter.updateSources(it)
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

    /**
     * Flowable that emits item when swipe to refresh is triggered by user.
     */
    private fun createRefreshFlowable(): Flowable<Any> {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh).toFlowable(BackpressureStrategy.BUFFER).startWith(true)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
    }

    /**
     * Adds labels to source list to show that source is watched by user.
     * @param context
     */
    private fun Flowable<List<NewsSourceAdapter.DataAdapterItem>>.addSourceWatchLabels(context: Context): Flowable<List<NewsSourceAdapter.NewsSourceAdapterItem>> {
        return this.map {
            val watchedLabel: String = context.getString(R.string.watched_sources)
            val sourcesLabel: String = context.getString(R.string.other_sources)

            val watchedSources = it.filter { it.source.watched }
            val otherSources = it.filter { !it.source.watched }

            val items = mutableListOf<NewsSourceAdapter.NewsSourceAdapterItem>()

            // Add watched label if there is any watched sources
            if (watchedSources.isNotEmpty()) {
                items.add(NewsSourceAdapter.LabelAdapterItem(watchedLabel))
                items.addAll(watchedSources)
            }

            if (otherSources.isNotEmpty()) {
                items.add(NewsSourceAdapter.LabelAdapterItem(sourcesLabel))
                items.addAll(otherSources)
            }

            items.toList()
        }
    }
}