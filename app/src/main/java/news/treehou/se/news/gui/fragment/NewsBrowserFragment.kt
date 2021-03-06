package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_news_browser.*
import news.treehou.se.news.R
import news.treehou.se.news.datasource.NewsApiSource
import news.treehou.se.news.gui.PageUtil
import news.treehou.se.news.gui.adapter.NewsArticlesAdapter
import javax.inject.Inject


class NewsBrowserFragment : BaseFragment(R.layout.fragment_news_browser) {

    @Inject lateinit var newsApi: NewsApiSource

    val searchSubject = BehaviorSubject.createDefault<String>("")

    private val adapter = NewsArticlesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsBrowserView.adapter = adapter
        newsBrowserView.layoutManager = LinearLayoutManager(context)
        newsBrowserView.itemAnimator = DefaultItemAnimator()
    }

    override fun onResume() {
        super.onResume()
        val context = context

        if (context != null) {
            Flowable.combineLatest<Any, String, String>(
                    createRefreshFlowable(),
                    searchSubject.toFlowable(BackpressureStrategy.LATEST),
                    BiFunction { _, searchText -> searchText }
            )
                    .switchMap {
                        if (it.isBlank()) {
                            Flowable.just(emptyList())
                        } else {
                            newsApi.getArticles(it)
                        }
                    }
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefresh.isRefreshing = false
                        errorText.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                        adapter.addArticles(it)
                    })

            adapter.getArticleSelectedFlowable()
                    .compose(bindToLifecycle())
                    .subscribe({ PageUtil.openArticlePage(context, it) })
        }
    }

    /**
     * Update the word to search for
     */
    fun search(searchWord: String) {
        searchSubject.onNext(searchWord)
    }

    /**
     * Flowable that emits item when swipe to refresh is triggered by user.
     */
    private fun createRefreshFlowable(): Flowable<Any> {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh).toFlowable(BackpressureStrategy.BUFFER).startWith(true)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
    }
}
