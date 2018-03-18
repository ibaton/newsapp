package news.treehou.se.news.gui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.util.Colors
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import news.treehou.se.news.R

/**
 * Main activity for application.
 * Provides news flow and options to navigate to other parts of application
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_news_source -> {
                openNewsSourcePageFlow()
            }
            R.id.nav_licenses -> {
                openLicensePageFlow()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
                openBrowseNewsPageFlow()
            }
        }
        return true
    }

    /**
     * Start the flow that opens news source page.
     */
    private fun openNewsSourcePageFlow() {
        startActivity(Intent(this, NewsSourcesActivity::class.java))
    }

    /**
     * Start the flow that opens news browser page.
     */
    private fun openBrowseNewsPageFlow() {
        startActivity(Intent(this, BrowseNewsActivity::class.java))
    }

    /**
     * Start the flow that opens license page.
     */
    private fun openLicensePageFlow() {
        val toolbarColor = ResourcesCompat.getColor(resources, R.color.actionbarBackground, theme)
        LibsBuilder().withActivityStyle(Libs.ActivityStyle.LIGHT)
                .withActivityColor(Colors(toolbarColor, toolbarColor))
                .withActivityTitle(getString(R.string.license))
                .start(this)
    }
}
