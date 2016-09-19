package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.liuqiang.gankforandroid.view.fragment.DailyDetailFragment
import org.joda.time.LocalDate

class DailyAdapter(val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var dates: List<LocalDate> = listOf()

    fun setData(dates: List<LocalDate>) {
        this.dates = dates
    }

    override fun getCount(): Int {
        return dates.size
    }

    override fun getItem(position: Int): Fragment {
        return DailyDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(DailyDetailFragment.KEY_DATE, dates[position])
            }
        }
    }

    override fun getItemPosition(obj: Any): Int {
        when (obj) {
            is DailyDetailFragment -> {
                val date = obj.arguments.getSerializable(DailyDetailFragment.KEY_DATE) as LocalDate
                for (i in dates.indices) {
                    if (date == dates[i]) {
                        return i
                    }
                }
            }
        }
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return dates[position].toString("yyyy/MM/dd")
    }
}
