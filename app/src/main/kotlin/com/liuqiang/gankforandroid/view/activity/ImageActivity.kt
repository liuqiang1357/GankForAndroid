package com.liuqiang.gankforandroid.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.liuqiang.gankforandroid.R
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.view.activity.base.SimpleBaseActivity
import com.liuqiang.gankforandroid.view.adapter.ImageAdapter
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : SimpleBaseActivity() {

    companion object {
        val KEY_IMAGES = "key_images"
        val KEY_POSITION = "key_position"

        fun startActivity(context: Context, images: List<Image>, position: Int) {
            context.startActivity(Intent(context, ImageActivity::class.java).apply {
                putExtra(KEY_IMAGES, images.toTypedArray())
                putExtra(KEY_POSITION, position)
            })
        }
    }

    override val layoutResourceId = R.layout.activity_image

    private lateinit var images: List<Image>
    private var position: Int = 0

    @Suppress("UNCHECKED_CAST")
    override fun initView(savedInstanceState: Bundle?) {
        images = (intent.getSerializableExtra(KEY_IMAGES) as Array<Image>).asList()
        position = intent.getIntExtra(KEY_POSITION, 0)

        view_pager.adapter = ImageAdapter(view_pager.context).apply { setData(images) }
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator_tv.text = "${position + 1}/${images.size}"
            }
        })

        indicator_tv.text = "${position + 1}/${images.size}"
        view_pager.setCurrentItem(position, false)
    }
}