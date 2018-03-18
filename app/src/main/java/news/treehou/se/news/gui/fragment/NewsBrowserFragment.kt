package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.net.toUri
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_browser.*
import news.treehou.se.news.R
import news.treehou.se.news.datasource.NewsApiSource
import news.treehou.se.news.gui.adapter.NewsArticlesAdapter
import news.treehou.se.news.model.NewsArticle
import javax.inject.Inject


class NewsBrowserFragment : BaseFragment(R.layout.fragment_news_browser) {

    @Inject lateinit var newsApi: NewsApiSource

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
            createRefreshFlowable()
                    .switchMap { newsApi.getTopHeadlines() }
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        swipeRefresh.isRefreshing = false
                        errorText.visibility = if(it.isEmpty()) View.VISIBLE  else View.GONE
                        adapter.addArticles(it)
                    })

            adapter.getArticleSelectedFlowable()
                    .compose(bindToLifecycle())
                    .subscribe({ openArticlePage(it) })
        }
    }

    /**
     * Open article in external browser tab customized with our theme.
     */
    private fun openArticlePage(article: NewsArticle) {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ResourcesCompat.getColor(resources, R.color.actionbarBackground, context?.theme))
                .setSecondaryToolbarColor(ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme))
                .build()
        customTabsIntent.launchUrl(context, article.url.toUri())
    }

    /**
     * Flowable that emits item when swipe to refresh is triggered by user.
     */
    private fun createRefreshFlowable() : Flowable<Any> {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh).toFlowable(BackpressureStrategy.BUFFER).startWith(true)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
    }
}
