package news.treehou.se.news.gui.activity

import android.os.Bundle
import android.view.MenuItem
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_news_sources.*
import news.treehou.se.news.R


class NewsSourcesActivity : BaseActivity(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_sources)
        setupActionbar()
    }

    private fun setupActionbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
