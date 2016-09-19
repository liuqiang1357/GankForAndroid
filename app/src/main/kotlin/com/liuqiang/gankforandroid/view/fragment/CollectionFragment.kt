package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.presenter.CollectionPresenter
import com.liuqiang.gankforandroid.util.TypeUtils
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.WebActivity
import com.liuqiang.gankforandroid.view.adapter.CollectionAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_collection.*

class CollectionFragment : BaseFragment<CollectionPresenter, CollectionPresenter.View>(), CollectionPresenter.View {

    override val layoutResourceId = R.layout.fragment_collection

    private var spinnerSelectedPosition: Int = 0

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        toolbar.setTitle(R.string.nav_type)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        spinner.adapter = ArrayAdapter<String>(toolbar.context, android.R.layout.simple_list_item_1,
                listOf(context.getString(R.string.all))
                        + TypeUtils.TYPES.map { TypeUtils.getLocalString(context, it) })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinnerSelectedPosition != spinner.selectedItemPosition) {
                    spinnerSelectedPosition = spinner.selectedItemPosition
                    loadBookmarksBySpinnerSelectedPosition()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (spinnerSelectedPosition != spinner.selectedItemPosition) {
                    spinnerSelectedPosition = spinner.selectedItemPosition
                    loadBookmarksBySpinnerSelectedPosition()
                }
            }
        }

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { loadBookmarksBySpinnerSelectedPosition() }

        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.layoutManager = LinearLayoutManager(context)

        recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
                .marginResId(R.dimen.divider_left_margin, R.dimen.divider_right_margin)
                .build())

        recycler_view.adapter = CollectionAdapter(recycler_view.context).apply {
            onBookmarkClickListener = { bookmarks, position ->
                WebActivity.startActivity(context, bookmarks[position])
            }
        }
    }

    override fun startPresenter() {
        presenter.loadAllBookmarks()
    }

    override fun refreshView() {
        super.refreshView()
        showBookmarks(presenter.bookmarks)
    }

    private fun loadBookmarksBySpinnerSelectedPosition() {
        if (spinnerSelectedPosition < 0) {
            showBookmarks(listOf())
        } else if (spinnerSelectedPosition == 0) {
            presenter.loadAllBookmarks()
        } else {
            presenter.loadBookmarksByType(TypeUtils.TYPES[spinnerSelectedPosition - 1])
        }
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showBookmarks(bookmarks: List<Bookmark>?) {
        safeLet(bookmarks) {
            val adapter = recycler_view.adapter as CollectionAdapter
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        }
    }
}
