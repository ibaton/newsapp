package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
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
            createRefreshObservable()
                    .switchMap({newsApi.getSources()})
                    .compose(bindToLifecycle())
                    .map { sources -> sources.sortedWith(compareBy({ !it.watched }, { it.name })) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefresh.isRefreshing = false
                        errorText.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
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

    private fun createRefreshObservable() : Flowable<Any> {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh).toFlowable(BackpressureStrategy.BUFFER).startWith(true)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
    }
}