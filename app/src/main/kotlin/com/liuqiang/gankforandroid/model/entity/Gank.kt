package com.liuqiang.gankforandroid.model.entity

import com.alibaba.fastjson.annotation.JSONField
import java.io.Serializable

data class Gank(
        @field:JSONField(name = "_id") var objectId: String = "",
        @field:JSONField(name = "type") var type: String = "",
        @field:JSONField(name = "desc") var desc: String = "",
        @field:JSONField(name = "url") var url: String = "",
        @field:JSONField(name = "who") var who: String? = null,
        @field:JSONField(name = "source") var source: String? = null,
        @field:JSONField(name = "used") var used: Boolean = false,
        @field:JSONField(name = "createdAt") var createdAt: String = "",
        @field:JSONField(name = "publishedAt") var publishedAt: String = ""
) : Serializable {

    fun toImage(): Image {
        val image = Image()
        image.objectId = objectId
        image.type = type
        image.desc = desc
        image.url = url
        image.who = who
        image.source = source
        image.used = used
        image.createdAt = createdAt
        image.publishedAt = publishedAt
        return image
    }

    fun toBookmark(): Bookmark {
        val bookmark = Bookmark()
        bookmark.objectId = objectId
        bookmark.type = type
        bookmark.desc = desc
        bookmark.url = url
        bookmark.who = who
        return bookmark
    }
}
