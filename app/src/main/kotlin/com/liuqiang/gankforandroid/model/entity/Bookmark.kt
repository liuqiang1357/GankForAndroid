package com.liuqiang.gankforandroid.model.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.annotation.Unique
import com.litesuits.orm.db.enums.AssignType
import java.io.Serializable

@Table("bookmark")
data class Bookmark(
        @field:PrimaryKey(AssignType.AUTO_INCREMENT) var id: Long = 0,
        @field:Unique @field:Column("objectId") var objectId: String = "",
        @field:Column("type") var type: String = "",
        @field:Column("desc") var desc: String = "",
        @field:Column("url") var url: String = "",
        @field:Column("who") var who: String? = null,
        @field:Column("collectedAt") var collectedAt: String = ""
) : Serializable
