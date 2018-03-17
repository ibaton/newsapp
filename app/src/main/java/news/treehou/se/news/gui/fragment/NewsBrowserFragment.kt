package news.treehou.se.news.gui.fragment

import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.net.toUri
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
            newsApi.getTopHeadlines()
                    .compose(bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if(it.isEmpty()){
                            errorText.visibility = View.VISIBLE
                            newsBrowserView.visibility = View.GONE
                        } else {
                            errorText.visibility = View.GONE
                            newsBrowserView.visibility = View.VISIBLE
                        }
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
}
