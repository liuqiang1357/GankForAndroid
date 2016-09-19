package com.liuqiang.gankforandroid.model.db.liteorm

import com.liuqiang.gankforandroid.BuildConfig
import com.liuqiang.gankforandroid.model.db.GankDataService
import com.liuqiang.gankforandroid.model.entity.Bookmark
import com.liuqiang.gankforandroid.model.entity.Image
import com.liuqiang.gankforandroid.model.entity.Type
import com.liuqiang.gankforandroid.util.TypeUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(23))
class LiteOrmHelperTest {

    lateinit var gankDataService: GankDataService

    @Before
    fun setUp() {
        gankDataService = LiteOrmHelper.getGankDataService(RuntimeEnvironment.application)
    }

    @Test
    fun testTypeList() {
        var types: List<Type>

        //初始化
        assertTrue(gankDataService.initTypes().toBlocking().single())
        //检查是否初始化成功
        assertEquals(TypeUtils.TYPES.size, gankDataService.getAllTypes().toBlocking().single().size)

        //获取全部
        types = gankDataService.getAllTypes().toBlocking().single()
        //第0项放到最后
        types[0].sort = 100
        //更新
        assertTrue(gankDataService.updateTypes(types).toBlocking().single() > 0)
        //检查是否更新成功
        assertEquals(types.sortedBy { it.sort }, gankDataService.getAllTypes().toBlocking().single())

        //获取全部
        types = gankDataService.getAllTypes().toBlocking().single()
        //第1项不可见
        types[1].visibility = false
        //更新
        assertTrue(gankDataService.updateTypes(types).toBlocking().single() > 0)
        //检查是否更新成功
        assertEquals(types, gankDataService.getAllTypes().toBlocking().single())

        //获取可见项
        assertEquals(types - listOf(types[1]), gankDataService.getVisibleTypes().toBlocking().single())
    }

    @Test
    fun testBookmarkList() {
        val bookmark0 = Bookmark()
        bookmark0.objectId = "objectId0"
        bookmark0.type = "type0"

        val bookmark1 = Bookmark()
        bookmark1.objectId = "objectId1"
        bookmark1.type = "type1"

        //添加一项
        assertTrue(gankDataService.addBookmark(bookmark0).toBlocking().single() > 0)
        //检查是否添加成功
        assertEquals(listOf(bookmark0), gankDataService.getAllBookmarks().toBlocking().single())

        //添加另一项
        assertTrue(gankDataService.addBookmark(bookmark1).toBlocking().single() > 0)
        //检查是否添加成功
        assertEquals(listOf(bookmark1,bookmark0), gankDataService.getAllBookmarks().toBlocking().single())

        //重复添加
        assertTrue(gankDataService.addBookmark(bookmark1).toBlocking().single() > 0)
        //检查是否重复添加
        assertEquals(listOf(bookmark1, bookmark0), gankDataService.getAllBookmarks().toBlocking().single())

        //按objectId获取
        assertEquals(bookmark0, gankDataService.getBookmarkByObjectId(bookmark0.objectId).toBlocking().single())

        //按类型获取
        assertEquals(listOf(bookmark1), gankDataService.getBookmarksByType("type1").toBlocking().single())

        //按objectId删除
        assertTrue(gankDataService.removeBookmarkByObjectId(bookmark0.objectId).toBlocking().single() > 0)
        //检查是否删除成功
        assertEquals(null, gankDataService.getBookmarkByObjectId(bookmark0.objectId).toBlocking().single())
    }

    @Test
    fun testImageList() {
        val image0 = Image()
        image0.objectId = "objectId0"

        val image1 = Image()
        image1.objectId = "objectId1"

        val image2 = Image()
        image1.objectId = "objectId2"

        //添加一项
        assertTrue(gankDataService.addImages(listOf(image0)).toBlocking().single() > 0)

        //检查是否添加成功
        assertEquals(listOf(image0), gankDataService.getAllImages().toBlocking().single())

        //添加另一项
        assertTrue(gankDataService.addImages(listOf(image1)).toBlocking().single() > 0)
        //检查是否添加成功
        assertEquals(listOf(image0, image1), gankDataService.getAllImages().toBlocking().single())

        //重复添加
        assertTrue(gankDataService.addImages(listOf(image1)).toBlocking().single() > 0)
        //检查是否重复添加
        assertEquals(listOf(image0, image1), gankDataService.getAllImages().toBlocking().single())

        //按images获取
        assertEquals(listOf(image2), gankDataService.getImages(listOf(image2)).toBlocking().single())
    }
}