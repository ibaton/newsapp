package news.treehou.se.news.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * A source of news articles.
 */
@Entity(tableName = "source")
data class NewsSource(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "name") val name: String,
        @Transient @ColumnInfo(name = "watched") var watched: Boolean
)