package com.liuqiang.gankforandroid.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.util.DisplayUtils
import com.liuqiang.gankforandroid.util.SettingUtils
import com.liuqiang.gankforandroid.view.activity.base.SimpleBaseActivity
import com.liuqiang.gankforandroid.view.fragment.CollectionFragment
import com.liuqiang.gankforandroid.view.fragment.DailyFragment
import com.liuqiang.gankforandroid.view.fragment.TypeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_navigation_main.view.*
import org.jetbrains.anko.contentView

class MainActivity : SimpleBaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override val layoutResourceId = R.layout.activity_main

    override val themeResourceId: Int
        get() {
            return if (SettingUtils.getTheme(this) == SettingUtils.THEME_LIGHT)
                R.style.AppThemeLight else R.style.AppThemeDark
        }

    private var lastBackPressedTime: Long = 0

    override fun initView(savedInstanceState: Bundle?) {
        DisplayUtils.updateWindowInsets(contentView!!)
        DisplayUtils.adaptTranslucentStatusBar(this)
        DisplayUtils.fitsStatusBarHeight(this, navigation_view.getHeaderView(0).status_bar)

        navigation_view.getHeaderView(0).apply {
            val theme = SettingUtils.getTheme(context)
            val resId = if (theme == SettingUtils.THEME_LIGHT)
                R.drawable.ic_theme_dark_36dp else R.drawable.ic_theme_light_36dp
            iv_switch_theme.setImageResource(resId)

            iv_switch_theme.setOnClickListener { v ->
                val nextTheme = if (theme == SettingUtils.THEME_LIGHT)
                    SettingUtils.THEME_DARK else SettingUtils.THEME_LIGHT
                val nextResId = if (nextTheme == SettingUtils.THEME_LIGHT)
                    R.drawable.ic_theme_dark_36dp else R.drawable.ic_theme_light_36dp
                iv_switch_theme.setImageResource(nextResId)
                SettingUtils.setTheme(context, nextTheme)
                window.setWindowAnimations(R.style.AppAnim_FadeInOut)
                recreate()
            }
        }

        navigation_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navigation_view.menu.getItem(0).isChecked = true
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_content, DailyFragment())
                    .commit()
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0)
        drawerToggle.syncState()
        drawer_layout.addDrawerListener(drawerToggle)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            } else {
                val duration = resources.getInteger(R.integer.press_to_exit_time)
                val current = System.currentTimeMillis()
                if (current - lastBackPressedTime > duration) {
                    val toast = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT)
                    toast.duration = duration
                    toast.show()
                    lastBackPressedTime = current
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_daily -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, DailyFragment())
                        .commit()
                item.isChecked = true
            }
            R.id.nav_type -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, TypeFragment())
                        .commit()
                item.isChecked = true
            }
            R.id.nav_collection -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, CollectionFragment())
                        .commit()
                item.isChecked = true
            }
            R.id.nav_setting -> {
                val duration = resources.getInteger(R.integer.medium_anim_time)
                contentView?.postDelayed({
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }, duration.toLong())
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return false
    }
}
