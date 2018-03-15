package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_browser.*
import news.treehou.se.news.R
import news.treehou.se.news.datasource.NewsApiSource
import news.treehou.se.news.gui.adapter.NewsArticlesAdapter
import news.treehou.se.news.model.NewsArticle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import androidx.net.toUri


class NewsBrowserFragment : BaseFragment(R.layout.fragment_news_browser) {

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
            val newsApi = NewsApiSource.getInstance(context.applicationContext)

            newsApi.getTopHeadlines()
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        adapter.addArticles(it)
                    })

            adapter.getArticleSelectedFlowable()
                    .compose(bindToLifecycle())
                    .subscribe({ openArticlePage(it) })
        }
    }

    private fun openArticlePage(article: NewsArticle){
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme))
                .setSecondaryToolbarColor(ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme))
                .build()
        customTabsIntent.launchUrl(context, article.url.toUri())
    }
}
