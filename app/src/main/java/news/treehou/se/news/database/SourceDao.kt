package news.treehou.se.news.database

import android.arch.persistence.room.*
import io.reactivex.Flowable
import news.treehou.se.news.model.NewsSource


@Dao
interface SourceDao {
    @get:Query("SELECT * FROM source")
    val all: Flowable<List<NewsSource>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg source: NewsSource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(vararg source: NewsSource)

    @Delete
    fun delete(source: NewsSource)
}