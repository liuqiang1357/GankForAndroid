package com.liuqiang.gankforandroid.view.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.liuqiang.gankforandroid.BuildConfig
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.util.SettingUtils
import com.liuqiang.gankforandroid.view.activity.base.SimpleSwipeBackActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : SimpleSwipeBackActivity() {

    override val layoutResourceId = R.layout.activity_setting

    override val themeResourceId: Int
        get() {
            return if (SettingUtils.getTheme(this) == SettingUtils.THEME_LIGHT)
                R.style.AppThemeLight_Translucent else R.style.AppThemeDark_Translucent
        }

    override fun initView(savedInstanceState: Bundle?) {

        toolbar.setTitle(R.string.nav_setting)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { this.finish() }

        if (SettingUtils.getOpenUrl(this) == SettingUtils.OPEN_URL_WEB_VIEW) {
            tv_summery_open_url.setText(R.string.open_url_web_view)
        }
        if (SettingUtils.getOpenUrl(this) == SettingUtils.OPEN_URL_BROWSER) {
            tv_summery_open_url.setText(R.string.open_url_browser)
        }
        item_open_url.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(R.string.open_url)
                    .setItems(arrayOf(getString(R.string.open_url_web_view),
                            getString(R.string.open_url_browser))
                            , { dialog, which ->
                        if (which == 0) {
                            SettingUtils.setOpenUrl(this, SettingUtils.OPEN_URL_WEB_VIEW)
                            tv_summery_open_url.setText(R.string.open_url_web_view)
                        }
                        if (which == 1) {
                            SettingUtils.setOpenUrl(this, SettingUtils.OPEN_URL_BROWSER)
                            tv_summery_open_url.setText(R.string.open_url_browser)
                        }
                    }).show()
        }

        if(BuildConfig.USE_LOCAL_DATA){
            if (SettingUtils.getDataSource(this) == SettingUtils.DATA_SOURCE_LOCAL) {
                tv_summery_data_source.setText(R.string.data_source_local)
            }
            if (SettingUtils.getDataSource(this) == SettingUtils.OPEN_URL_BROWSER) {
                tv_summery_data_source.setText(R.string.data_source_remote)
            }
            item_data_source.setOnClickListener {
                AlertDialog.Builder(this)
                        .setTitle(R.string.data_source)
                        .setItems(arrayOf(getString(R.string.data_source_local),
                                getString(R.string.data_source_remote))
                                , { dialog, which ->
                            if (which == 0) {
                                SettingUtils.setDataSource(this, SettingUtils.DATA_SOURCE_LOCAL)
                                tv_summery_data_source.setText(R.string.data_source_local)
                            }
                            if (which == 1) {
                                SettingUtils.setDataSource(this, SettingUtils.OPEN_URL_BROWSER)
                                tv_summery_data_source.setText(R.string.data_source_remote)
                            }
                        }).show()
            }
        } else {
            item_data_source.visibility = View.GONE
        }
    }
}
