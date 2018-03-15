package news.treehou.se.news.newsapi.message

import news.treehou.se.news.model.NewsArticle
import news.treehou.se.news.model.NewsSource

object Message {
    data class SourceMessage(val status: String, val sources: List<NewsSource> = listOf())
    data class ArticleMessage(val status: String, val articles: List<NewsArticle> = listOf())
}