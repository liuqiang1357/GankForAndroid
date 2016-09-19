package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.presenter.DailyPresenter
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.adapter.DailyAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_daily.*
import org.joda.time.LocalDate

class DailyFragment : BaseFragment<DailyPresenter, DailyPresenter.View>(), DailyPresenter.View {

    override val layoutResourceId = R.layout.fragment_daily

    private var dates: List<LocalDate> = listOf()

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        toolbar.setTitle(R.string.nav_daily)
        setSupportActionBar(toolbar)

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { presenter.loadHistoryData() }

        view_pager.adapter = DailyAdapter(view_pager.context, childFragmentManager)
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateToolbar(position)
            }
        })
    }

    override fun startPresenter() {
        presenter.loadHistoryData()
    }

    override fun refreshView() {
        super.refreshView()
        showDates(presenter.dates)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.daily, menu)
        val subMenu = menu.findItem(R.id.action_history).subMenu
        subMenu.clear()
        for (i in dates.indices) {
            subMenu.add(0, R.id.action_history_item, i, dates[i].toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> return true
            R.id.action_history_item -> {
                view_pager.currentItem = item.order
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateToolbar(position: Int) {
        if (dates.size == 0) {
            toolbar.setTitle(R.string.nav_daily)
        } else if (position == 0) {
            toolbar.setTitle(R.string.newest_gank)
        } else if (position < dates.size) {
            toolbar.title = dates[position].toString("yyyy/MM/dd")
        }
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showDates(dates: List<LocalDate>?) {
        safeLet(dates) {
            val adapter = view_pager.adapter as DailyAdapter
            adapter.setData(it)
            adapter.notifyDataSetChanged()

            this.dates = it
            activity.supportInvalidateOptionsMenu()
            updateToolbar(view_pager.currentItem)

            swipe_refresh_layout.isEnabled = false
        }
    }
}
