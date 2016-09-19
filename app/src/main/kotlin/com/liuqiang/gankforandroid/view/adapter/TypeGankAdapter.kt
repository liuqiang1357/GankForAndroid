package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.util.IconUtils
import com.liuqiang.gankforandroid.util.inflate
import com.liuqiang.gankforandroid.util.noThrow
import kotlinx.android.synthetic.main.item_list_type_gank.view.*
import org.joda.time.DateTime

class TypeGankAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onTypeGankClickListener: ((ganks: List<Gank>, position: Int) -> Unit)? = null

    private var ganks: List<Gank> = listOf()

    fun setData(ganks: List<Gank>) {
        this.ganks = ganks
    }

    override fun getItemCount(): Int {
        return ganks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TypeGankViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeGankViewHolder -> {
                val showHeader = if (position != 0) {
                    val dateTime = noThrow { DateTime(ganks[position].publishedAt) }
                    val previousDateTime = noThrow { DateTime(ganks[position - 1].publishedAt) }
                    dateTime?.withTimeAtStartOfDay() != previousDateTime?.withTimeAtStartOfDay()
                } else true

                holder.bindData(ganks[position], showHeader)
                holder.onItemClickListener = {
                    onTypeGankClickListener?.invoke(ganks, position)
                }
            }
        }
    }

    private class TypeGankViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_type_gank, parent, false)) {

        var onItemClickListener: (() -> Unit)? = null

        fun bindData(gank: Gank, showHeader: Boolean) {
            itemView.apply {
                val dateTime = noThrow { DateTime(gank.publishedAt) }
                tv_date.text = dateTime?.toLocalDate().toString()

                iv_icon.setImageResource(IconUtils.getIconResId(gank.url, gank.type))
                tv_desc.text = gank.desc

                tv_who.visibility = if (gank.who != null) View.VISIBLE else View.GONE
                tv_who.text = gank.who

                item_header.visibility = if (showHeader && dateTime != null) View.VISIBLE else View.GONE
                item_content.setOnClickListener { onItemClickListener?.invoke() }
            }
        }
    }
}
