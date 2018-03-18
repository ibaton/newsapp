package news.treehou.se.news.newsapi

import java.text.SimpleDateFormat
import java.util.*

/**
 * Helps to handle dates received by news source api.
 */
class DateUtil {
    companion object {

        // Date format used to parse news api date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    }
}