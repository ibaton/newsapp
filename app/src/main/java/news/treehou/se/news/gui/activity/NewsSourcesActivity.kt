package news.treehou.se.news.gui.activity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_news_sources.*
import news.treehou.se.news.R

/**
 * Activity that lets user select which news sources to follow
 */
class NewsSourcesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_sources)
        setupActionbar()
    }

    private fun setupActionbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
