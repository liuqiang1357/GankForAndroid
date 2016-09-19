package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Gank
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.util.inflate
import kotlinx.android.synthetic.main.item_list_daily_gank.view.*
import kotlinx.android.synthetic.main.item_list_daily_girl.view.*

class DailyDetailAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onDailyGirlClickListener: ((images: List<Image>, position: Int) -> Unit)? = null
    var onDailyGankClickListener: ((ganks: List<Gank>, position: Int) -> Unit)? = null

    private var images: List<Image> = listOf()
    private var ganks: List<Gank> = listOf()

    fun setData(images: List<Image>, ganks: List<Gank>) {
        this.images = images
        this.ganks = ganks
    }

    override fun getItemCount(): Int {
        return images.size + ganks.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < images.size) DailyGirlViewHolder::class.java.hashCode()
        else DailyGankViewHolder::class.java.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            DailyGirlViewHolder::class.java.hashCode() -> return DailyGirlViewHolder(parent)
            DailyGankViewHolder::class.java.hashCode() -> return DailyGankViewHolder(parent)
        }
        throw AssertionError("view type not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DailyGirlViewHolder -> {
                val itemPosition = position

                holder.bindData(images[itemPosition])
                holder.onItemClickListener = {
                    onDailyGirlClickListener?.invoke(images, itemPosition)
                }
            }
            is DailyGankViewHolder -> {
                val itemPosition = position - images.size

                val showHeader = if (itemPosition != 0) {
                    ganks[itemPosition].type != ganks[itemPosition - 1].type
                } else true
                val showTop = itemPosition == 0
                val showBottom = itemPosition == ganks.size - 1

                holder.bindData(ganks[itemPosition], showHeader, showTop, showBottom)
                holder.onItemClickListener = {
                    onDailyGankClickListener?.invoke(ganks, itemPosition)
                }
            }
        }
    }

    private class DailyGirlViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_daily_girl, parent, false)) {

        var onItemClickListener: (() -> Unit)? = null

        fun bindData(image: Image) {
            itemView.apply {
                Glide.with(context).load(image.url).into(iv_girl)

                setOnClickListener { onItemClickListener?.invoke() }
            }
        }
    }

    private class DailyGankViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_daily_gank, parent, false)) {

        var onItemClickListener: (() -> Unit)? = null

        fun bindData(gank: Gank, showHeader: Boolean, showTop: Boolean, showBottom: Boolean) {
            itemView.apply {
                tv_type.text = gank.type

                var builder = SpannableStringBuilder(gank.desc)
                if (gank.who != null) {
                    val viaString = SpannableString("(${gank.who})")
                    val typedValue = TypedValue()
                    if (context.theme.resolveAttribute(R.attr.textColorTertiary, typedValue, true)) {
                        viaString.setSpan(ForegroundColorSpan(typedValue.data), 0, viaString.length, 0)
                    }
                    builder = builder.append(viaString)
                }
                tv_desc.text = builder.subSequence(0, builder.length)

                item_header.visibility = if (showHeader) View.VISIBLE else View.GONE
                item_content.setOnClickListener { onItemClickListener?.invoke() }

                item_top_space.visibility = if (showTop) View.VISIBLE else View.GONE
                item_bottom_space.visibility = if (showBottom) View.VISIBLE else View.GONE
            }
        }
    }
}
