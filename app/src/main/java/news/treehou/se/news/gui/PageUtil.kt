package news.treehou.se.news.gui

import android.content.Context
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import androidx.net.toUri
import news.treehou.se.news.R
import news.treehou.se.news.model.NewsArticle

class PageUtil {
    companion object {

        /**
         * Open article in external browser tab customized with our theme.
         */
        fun openArticlePage(context: Context, article: NewsArticle) {
            val resources = context.resources
            val customTabsIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(ResourcesCompat.getColor(resources, R.color.actionbarBackground, context.theme))
                    .setSecondaryToolbarColor(ResourcesCompat.getColor(resources, R.color.colorAccent, context.theme))
                    .build()
            customTabsIntent.launchUrl(context, article.url.toUri())
        }
    }
}