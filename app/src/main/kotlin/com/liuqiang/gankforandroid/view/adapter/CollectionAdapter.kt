package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.util.IconUtils
import com.liuqiang.gankforandroid.util.inflate
import com.liuqiang.gankforandroid.util.noThrow
import kotlinx.android.synthetic.main.item_list_type_gank.view.*
import org.joda.time.DateTime

class CollectionAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onBookmarkClickListener: ((bookmarks: List<Bookmark>, position: Int) -> Unit)? = null

    private var bookmarks: List<Bookmark> = listOf()

    fun setData(bookmarks: List<Bookmark>) {
        this.bookmarks = bookmarks
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookmarkViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookmarkViewHolder -> {
                val showHeader = if (position != 0) {
                    val dateTime = noThrow { DateTime(bookmarks[position].collectedAt) }
                    val previousDateTime = noThrow { DateTime(bookmarks[position - 1].collectedAt) }
                    dateTime?.withTimeAtStartOfDay() != previousDateTime?.withTimeAtStartOfDay()
                } else true

                holder.bindData(bookmarks[position], showHeader)
                holder.onItemClickListener = {
                    onBookmarkClickListener?.invoke(bookmarks, position)
                }
            }
        }
    }

    private class BookmarkViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_bookmark, parent, false)) {

        var onItemClickListener: (() -> Unit)? = null

        fun bindData(bookmark: Bookmark, showHeader: Boolean) {
            itemView.apply {
                val dateTime = noThrow { DateTime(bookmark.collectedAt) }
                tv_date.text = dateTime?.toLocalDate().toString()

                iv_icon.setImageResource(IconUtils.getIconResId(bookmark.url, bookmark.type))
                tv_desc.text = bookmark.desc

                tv_who.visibility = if (bookmark.who != null) View.VISIBLE else View.GONE
                tv_who.text = bookmark.who

                item_header.visibility = if (showHeader && dateTime != null) View.VISIBLE else View.GONE
                item_content.setOnClickListener { onItemClickListener?.invoke() }
            }
        }
    }
}
