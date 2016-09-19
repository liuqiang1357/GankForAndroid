package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.graphics.drawable.PaintDrawable
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.util.inflate
import kotlinx.android.synthetic.main.item_list_type_girl.view.*

class TypeGirlAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onTypeGirlClickListener: ((images: List<Image>, position: Int) -> Unit)? = null

    private var images: List<Image> = listOf()

    fun setData(images: List<Image>) {
        this.images = images
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TypeGirlViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeGirlViewHolder -> {
                holder.bindData(images[position])
                holder.onItemClickListener = {
                    onTypeGirlClickListener?.invoke(images, position)
                }
            }
        }
    }

    private class TypeGirlViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_type_girl, parent, false)) {

        var onItemClickListener: (() -> Unit)? = null

        fun bindData(image: Image) {
            itemView.apply {
                val drawable = PaintDrawable(0)
                drawable.intrinsicWidth = image.width
                drawable.intrinsicHeight = image.height
                Glide.with(context).load(image.url).placeholder(drawable).into(iv_girl)

                setOnClickListener { onItemClickListener?.invoke() }
            }
        }
    }
}
