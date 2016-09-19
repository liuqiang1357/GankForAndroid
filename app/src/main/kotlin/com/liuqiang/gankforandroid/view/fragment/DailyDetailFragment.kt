package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.presenter.DailyDetailPresenter
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.ImageActivity
import com.liuqiang.gankforandroid.view.activity.WebActivity
import com.liuqiang.gankforandroid.view.adapter.DailyDetailAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_daily_detail.*
import org.joda.time.LocalDate

class DailyDetailFragment : BaseFragment<DailyDetailPresenter, DailyDetailPresenter.View>(), DailyDetailPresenter.View {

    companion object {
        val KEY_DATE = "key_date"
    }

    override val layoutResourceId = R.layout.fragment_daily_detail

    private lateinit var date: LocalDate

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        date = arguments.getSerializable(KEY_DATE) as LocalDate

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { presenter.loadDailyData(date) }

        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.layoutManager = LinearLayoutManager(context)

        recycler_view.adapter = DailyDetailAdapter(recycler_view.context).apply {
            onDailyGankClickListener = { ganks, position ->
                WebActivity.startActivity(context, ganks[position].toBookmark())
            }

            onDailyGirlClickListener = { images, position ->
                ImageActivity.startActivity(context, images, position)
            }
        }
    }

    override fun startPresenter() {
        presenter.loadDailyData(date)
    }

    override fun refreshView() {
        super.refreshView()
        showImagesAndGanks(presenter.images, presenter.ganks)
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showImagesAndGanks(images: List<Image>?, ganks: List<Gank>?) {
        safeLet(images, ganks) { images, ganks ->
            val adapter = recycler_view.adapter as DailyDetailAdapter
            adapter.setData(images, ganks)
            adapter.notifyDataSetChanged()
        }
    }
}
