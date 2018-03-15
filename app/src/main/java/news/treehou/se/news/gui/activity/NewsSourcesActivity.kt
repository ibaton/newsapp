package news.treehou.se.news.gui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import news.treehou.se.news.R

import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_news_sources.*


class NewsSourcesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_sources)
        setupActionbar()
    }

    private fun setupActionbar(){
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
