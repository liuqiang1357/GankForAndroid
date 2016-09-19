package com.liuqiang.gankforandroid.model.db

import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Type
import rx.Observable

interface GankDataService {

    fun initTypes(): Observable<Boolean>

    fun updateTypes(types: List<Type>): Observable<Int>

    fun getAllTypes(): Observable<List<Type>>

    fun getVisibleTypes(): Observable<List<Type>>


    fun addBookmark(bookmark: Bookmark): Observable<Long>

    fun removeBookmarkByObjectId(objectId: String): Observable<Int>

    fun getBookmarkByObjectId(objectId: String): Observable<Bookmark?>

    fun getAllBookmarks(): Observable<List<Bookmark>>

    fun getBookmarksByType(type: String): Observable<List<Bookmark>>


    fun addImages(images: List<Image>): Observable<Int>

    fun getAllImages(): Observable<List<Image>>

    fun getImages(images: List<Image>): Observable<List<Image>>
}
