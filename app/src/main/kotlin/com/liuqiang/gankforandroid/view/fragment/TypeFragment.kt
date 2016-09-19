package com.liuqiang.gankforandroid.view.fragment

import android.os.Bundle
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.presenter.TypePresenter
import com.liuqiang.gankforandroid.util.safeLet
import com.liuqiang.gankforandroid.view.adapter.TypeAdapter
import com.liuqiang.gankforandroid.view.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_type.*

class TypeFragment : BaseFragment<TypePresenter, TypePresenter.View>(), TypePresenter.View {

    override val layoutResourceId = R.layout.fragment_type

    override fun doInject() {
        fragmentComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.setTitle(R.string.nav_type)
        setSupportActionBar(toolbar)

        view_pager.adapter = TypeAdapter(view_pager.context, childFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun startPresenter() {
        presenter.loadVisibleTypes()
    }

    override fun refreshView() {
        super.refreshView()
        showTypes(presenter.types)
    }

    override fun showTypes(types: List<Type>?) {
        safeLet(types) {
            val adapter = view_pager.adapter as TypeAdapter
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        }
    }
}
