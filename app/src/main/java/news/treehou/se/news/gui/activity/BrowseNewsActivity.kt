package news.treehou.se.news.gui.activity

import android.os.Bundle
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_news_browser.*
import news.treehou.se.news.R
import news.treehou.se.news.gui.fragment.NewsBrowserFragment
import java.util.concurrent.TimeUnit

/**
 * Activity used to search for news
 */
class BrowseNewsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_browser)
        setupActionbar()
        setupSearchFlowable()
    }

    private fun setupActionbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Setup rx flow listening to search field and updates fragment with response.
     */
    private fun setupSearchFlowable() {
        RxTextView.textChanges(searchText)
                .debounce(1, TimeUnit.SECONDS) // Wait for a while after last letter is entered to prevent spamming server
                .compose(bindToLifecycle())
                .subscribe({
                    val fragment = newsBrowserFragment
                    if (fragment is NewsBrowserFragment) {
                        fragment.search(it.toString())
                    }
                })
    }
}
