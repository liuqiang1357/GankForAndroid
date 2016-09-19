package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.presenter.TypeGankPresenter
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.WebActivity
import com.liuqiang.gankforandroid.view.adapter.TypeGankAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_type_gank.*

class TypeGankFragment : BaseFragment<TypeGankPresenter, TypeGankPresenter.View>(), TypeGankPresenter.View {

    companion object {
        val KEY_TYPE = "key_type"

        fun newInstance(type: Type): TypeGankFragment {
            return TypeGankFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_TYPE, type)
                }
            }
        }
    }

    override val layoutResourceId = R.layout.fragment_type_gank

    private lateinit var type: Type

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = arguments.getSerializable(KEY_TYPE) as Type

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { presenter.loadTypeData(type) }

        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.layoutManager = LinearLayoutManager(context)

        recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
                .marginResId(R.dimen.divider_left_margin, R.dimen.divider_right_margin)
                .build())

        recycler_view.adapter = TypeGankAdapter(recycler_view.context).apply {
            onTypeGankClickListener = { ganks, position ->
                WebActivity.startActivity(context, ganks[position].toBookmark())
            }
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recycler_view.layoutManager as LinearLayoutManager
                val position = layoutManager.findLastVisibleItemPosition()
                if (position >= layoutManager.itemCount - 3 && dy > 0) {
                    if (presenter.loadMoreTypeData.isUnsubscribed) {
                        presenter.loadMoreTypeData(type)
                    }
                }
            }
        })
    }

    override fun startPresenter() {
        presenter.loadTypeData(type)
    }

    override fun refreshView() {
        super.refreshView()
        showGanks(presenter.ganks)
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showGanks(ganks: List<Gank>?) {
        safeLet(ganks) {
            val adapter = recycler_view.adapter as TypeGankAdapter
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showMoreGanks(ganks: List<Gank>?, start: Int, count: Int) {
        safeLet(ganks) {
            val adapter = recycler_view.adapter as TypeGankAdapter
            adapter.setData(it)
            adapter.notifyItemChanged(start, count)
        }
    }
}
