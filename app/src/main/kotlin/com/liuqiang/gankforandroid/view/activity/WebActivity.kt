package com.liuqiang.gankforandroid.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.presenter.WebPresenter
import com.liuqiang.gankforandroid.util.SettingUtils
import com.liuqiang.gankforandroid.util.SystemUtils
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.base.SwipeBackActivity
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : SwipeBackActivity<WebPresenter, WebPresenter.View>(), WebPresenter.View {

    companion object {
        val KEY_BOOKMARK = "key_bookmark"

        fun startActivity(context: Context, bookmark: Bookmark) {
            if (SettingUtils.getOpenUrl(context) == SettingUtils.OPEN_URL_BROWSER) {
                SystemUtils.browser(context, bookmark.url)
                return
            }
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(KEY_BOOKMARK, bookmark)
            })
        }
    }

    override val layoutResourceId = R.layout.activity_web

    override val themeResourceId: Int
        get() {
            return if (SettingUtils.getTheme(this) == SettingUtils.THEME_LIGHT)
                R.style.AppThemeLight_Translucent else R.style.AppThemeDark_Translucent
        }

    private lateinit var bookmark: Bookmark
    private var isCollected: Boolean? = null

    override fun doInject() {
        activityComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        bookmark = intent.getSerializableExtra(KEY_BOOKMARK) as Bookmark

        toolbar.title = bookmark.desc
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { this.finish() }

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { web_view.reload() }

        web_view.settings.javaScriptEnabled = true
        web_view.settings.setSupportZoom(true)
        web_view.settings.displayZoomControls = true

        web_view.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (isViewInitialized) {
                    swipe_refresh_layout.isRefreshing = true
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (isViewInitialized) {
                    swipe_refresh_layout.isRefreshing = false
                }
            }
        })

        if (savedInstanceState == null) {
            web_view.loadUrl(bookmark.url)
        } else {
            web_view.restoreState(savedInstanceState)
        }
    }

    override fun startPresenter() {
        presenter.loadBookmark(bookmark.objectId)
    }

    override fun refreshView() {
        super.refreshView()
        showIsCollected(presenter.isCollected)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.web, menu)
        val item = menu.findItem(R.id.action_collect)
        item.isEnabled = isCollected != null
        if (isCollected == true) item.setIcon(R.drawable.ic_menu_collected_white_24dp)
        if (isCollected == false) item.setIcon(R.drawable.ic_menu_uncollected_white_24dp)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_collect -> {
                if (isCollected == true) presenter.removeBookmark(bookmark.objectId)
                if (isCollected == false) presenter.addBookmark(bookmark)
                return true
            }
            R.id.action_copy_url -> SystemUtils.copyText(this, bookmark.url)
            R.id.action_open_in_browser -> SystemUtils.browser(this, bookmark.url)
            R.id.action_share -> SystemUtils.share(this, bookmark.desc, bookmark.url)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (web_view.canGoBack()) {
            web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showIsCollected(isCollected: Boolean?) {
        safeLet(isCollected) {
            this.isCollected = it
            this.supportInvalidateOptionsMenu()
        }
    }
}
