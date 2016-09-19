package com.liuqiang.gankforandroid.model.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.annotation.Unique
import com.litesuits.orm.db.enums.AssignType
import java.io.Serializable

@Table("image")
data class Image(
        @field:PrimaryKey(AssignType.AUTO_INCREMENT) var id: Long = 0,
        @field:Unique @field:Column("objectId") var objectId: String = "",
        @field:Column("type") var type: String = "",
        @field:Column("desc") var desc: String = "",
        @field:Column("url") var url: String = "",
        @field:Column("who") var who: String? = null,
        @field:Column("source") var source: String? = null,
        @field:Column("used") var used: Boolean = false,
        @field:Column("createdAt") var createdAt: String = "",
        @field:Column("publishedAt") var publishedAt: String = "",
        @field:Column("width") var width: Int = 0,
        @field:Column("height") var height: Int = 0
) : Serializable
