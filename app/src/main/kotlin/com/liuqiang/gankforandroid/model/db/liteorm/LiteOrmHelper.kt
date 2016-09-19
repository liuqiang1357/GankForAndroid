package com.liuqiang.gankforandroid.model.db.liteorm

import android.content.Context
import com.litesuits.orm.LiteOrm
import com.litesuits.orm.db.assit.QueryBuilder
import com.litesuits.orm.db.assit.WhereBuilder
import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.util.TypeUtils
import org.joda.time.DateTime
import rx.Observable
import java.util.*

object LiteOrmHelper {

    private var liteOrmDataService: LiteOrmDataService? = null

    fun getGankDataService(context: Context): GankDataService {
        synchronized(LiteOrmHelper::class.java) {
            var service = liteOrmDataService
            if (service == null || service.context != context.applicationContext) {
                service = LiteOrmDataService(context.applicationContext)
            }
            liteOrmDataService = service
            return service
        }
    }

    private class LiteOrmDataService(val context: Context) : GankDataService {

        private val liteOrm: LiteOrm

        init {
            liteOrm = LiteOrm.newSingleInstance(context, "liteorm.db")
        }

        override fun initTypes(): Observable<Boolean> {
            return Observable.defer {
                val types = ArrayList<Type>()
                for (i in TypeUtils.TYPES.indices) {
                    val type = Type()
                    type.type = TypeUtils.TYPES[i]
                    type.sort = i
                    type.visibility = true
                    types.add(type)
                }
                liteOrm.deleteAll(Type::class.java)
                val ret = liteOrm.save(types)
                Observable.just(ret != -1)
            }
        }

        override fun updateTypes(types: List<Type>): Observable<Int> {
            return Observable.defer {
                val ret = liteOrm.save(types)
                Observable.just(ret)
            }
        }

        override fun getAllTypes(): Observable<List<Type>> {
            return Observable.defer {
                val types = liteOrm.query(QueryBuilder(Type::class.java)
                        .appendOrderAscBy("sort"))
                Observable.just<List<Type>>(types)
            }
        }

        override fun getVisibleTypes(): Observable<List<Type>> {
            return Observable.defer {
                val types = liteOrm.query(QueryBuilder(Type::class.java)
                        .whereEquals("visibility", true)
                        .appendOrderAscBy("sort"))
                Observable.just<List<Type>>(types)
            }
        }

        override fun addBookmark(bookmark: Bookmark): Observable<Long> {
            return Observable.defer {
                bookmark.collectedAt = DateTime.now().toString()
                val ret = liteOrm.save(bookmark)
                Observable.just(ret)
            }
        }

        override fun removeBookmarkByObjectId(objectId: String): Observable<Int> {
            return Observable.defer {
                val ret = liteOrm.delete(WhereBuilder(Bookmark::class.java)
                        .equals("objectId", objectId))
                Observable.just(ret)
            }
        }

        override fun getBookmarkByObjectId(objectId: String): Observable<Bookmark?> {
            return Observable.defer {
                val bookmarks = liteOrm.query(QueryBuilder(Bookmark::class.java)
                        .whereEquals("objectId", objectId))
                Observable.just(bookmarks.firstOrNull())
            }
        }

        override fun getAllBookmarks(): Observable<List<Bookmark>> {
            return Observable.defer {
                val bookmarks = liteOrm.query(QueryBuilder(Bookmark::class.java)
                        .appendOrderDescBy("collectedAt"))
                Observable.just<List<Bookmark>>(bookmarks)
            }
        }

        override fun getBookmarksByType(type: String): Observable<List<Bookmark>> {
            return Observable.defer {
                val bookmarks = liteOrm.query(QueryBuilder(Bookmark::class.java)
                        .whereEquals("type", type).appendOrderDescBy("collectedAt"))
                Observable.just<List<Bookmark>>(bookmarks)
            }
        }

        override fun addImages(images: List<Image>): Observable<Int> {
            return Observable.defer {
                val ret = liteOrm.save(images)
                Observable.just(ret)
            }
        }

        override fun getAllImages(): Observable<List<Image>> {
            return Observable.defer {
                val images = liteOrm.query(QueryBuilder(Image::class.java)
                        .appendOrderDescBy("publishedAt"))
                Observable.just<List<Image>>(images)
            }
        }

        override fun getImages(images: List<Image>): Observable<List<Image>> {
            return Observable.defer {
                val ret = ArrayList<Image>()
                for (image in images) {
                    val temp = liteOrm.query(QueryBuilder(Image::class.java)
                            .whereEquals("objectId", image.objectId))
                    if (temp.isEmpty()) {
                        ret.add(image)
                    } else {
                        ret.add(temp.first())
                    }
                }
                Observable.just<List<Image>>(ret)
            }
        }
    }
}
