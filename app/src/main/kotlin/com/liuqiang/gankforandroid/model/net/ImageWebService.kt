package com.liuqiang.gankforandroid.model.net

import com.liuqiang.gankforandroid.model.entity.Image
import rx.Observable

interface ImageWebService {

    fun getImageSize(image: Image): Observable<Image>
}