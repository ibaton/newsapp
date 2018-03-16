package news.treehou.se.news.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import news.treehou.se.news.model.NewsSource
import android.arch.persistence.room.Room
import android.content.Context


@Database(entities = [NewsSource::class], version = 1)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun sourceDao(): SourceDao

    companion object {
        fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, NewsDatabase::class.java, "news.db")
                        .build()
    }
}


