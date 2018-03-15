package news.treehou.se.news.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * A source of news articles.
 */
@Entity(tableName = "source")
data class NewsArticle(
        val title: String,
        val description: String,
        val author: String?,
        val url: String,
        val urlToImage: String?,
        val publishedAt: String?,
        val source: NewsSource
)