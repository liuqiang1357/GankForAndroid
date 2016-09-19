package com.liuqiang.gankforandroid.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.util.inflate
import kotlinx.android.synthetic.main.item_pager_image.view.*
import uk.co.senab.photoview.PhotoViewAttacher

class ImageAdapter(val context: Context) : PagerAdapter() {

    private var images: List<Image> = listOf()

    fun setData(images: List<Image>) {
        this.images = images
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = container.inflate(R.layout.item_pager_image, container, false)
        Glide.with(context).load(images[position].url).asBitmap().placeholder(R.drawable.icon_image_loading)
                .error(R.drawable.icon_image_failure).into(object : SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                view.iv_image.setImageBitmap(resource)
                PhotoViewAttacher(view.iv_image)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                view.iv_image.setImageDrawable(placeholder)
            }

            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                view.iv_image.setImageDrawable(errorDrawable)
            }
        })
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}