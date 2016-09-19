package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.util.TypeUtils
import com.liuqiang.gankforandroid.view.fragment.TypeGankFragment
import com.liuqiang.gankforandroid.view.fragment.TypeGirlFragment

class TypeAdapter(val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var types: List<Type> = listOf()

    fun setData(types: List<Type>) {
        this.types = types
    }

    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(position: Int): Fragment {
        return if (TypeUtils.TYPE_GIRL == types[position].type) {
            TypeGirlFragment.newInstance(types[position])
        } else {
            TypeGankFragment.newInstance(types[position])
        }
    }

    override fun getItemPosition(obj: Any): Int {
        when (obj) {
            is TypeGirlFragment -> {
                val type = obj.arguments.getSerializable(TypeGirlFragment.KEY_TYPE) as Type
                for (i in types.indices) {
                    if (type == types[i]) {
                        return i
                    }
                }
            }
            is TypeGankFragment -> {
                val type = obj.arguments.getSerializable(TypeGankFragment.KEY_TYPE) as Type
                for (i in types.indices) {
                    if (type == types[i]) {
                        return i
                    }
                }
            }
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TypeUtils.getLocalString(context, types[position].type)
    }
}
