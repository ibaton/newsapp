package news.treehou.se.news.datasource

import io.reactivex.Flowable
import news.treehou.se.news.model.NewsSource

fun Flowable<List<NewsSource>>.filterWatchedSources(): Flowable<List<NewsSource>> {
    return this.map { it.filter { source -> source.watched } }
}