package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.presenter.TypeGirlPresenter
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.activity.ImageActivity
import com.liuqiang.gankforandroid.view.adapter.TypeGirlAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_type_girl.*

class TypeGirlFragment : BaseFragment<TypeGirlPresenter, TypeGirlPresenter.View>(), TypeGirlPresenter.View {

    companion object {
        val KEY_TYPE = "key_type"

        fun newInstance(type: Type): TypeGirlFragment {
            return TypeGirlFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_TYPE, type)
                }
            }
        }
    }

    override val layoutResourceId = R.layout.fragment_type_girl

    private lateinit var type: Type

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = arguments.getSerializable(KEY_TYPE) as Type

        swipe_refresh_layout.setColorSchemeResources(R.color.md_blue_600)
        swipe_refresh_layout.setOnRefreshListener { presenter.loadTypeData(type) }

        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recycler_view.adapter = TypeGirlAdapter(recycler_view.context).apply {
            onTypeGirlClickListener = { images, position ->
                ImageActivity.startActivity(context, images, position)
            }
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recycler_view.layoutManager as StaggeredGridLayoutManager
                val positions = layoutManager.findLastVisibleItemPositions(null)
                for (position in positions) {
                    if (position >= layoutManager.itemCount - 3 && dy > 0) {
                        if (presenter.loadMoreTypeData.isUnsubscribed) {
                            presenter.loadMoreTypeData(type)
                        }
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
        showImages(presenter.images)
    }

    override fun showBaseLoading(loading: Boolean) {
        swipe_refresh_layout.isRefreshing = loading
    }

    override fun showImages(images: List<Image>?) {
        safeLet(images) {
            val adapter = recycler_view.adapter as TypeGirlAdapter
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showMoreImages(images: List<Image>?, start: Int, count: Int) {
        safeLet(images) {
            val adapter = recycler_view.adapter as TypeGirlAdapter
            adapter.setData(it)
            adapter.notifyItemChanged(start, count)
        }
    }
}
