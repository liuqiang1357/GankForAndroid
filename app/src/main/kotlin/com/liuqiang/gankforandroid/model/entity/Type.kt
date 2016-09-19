package com.liuqiang.gankforandroid.model.entity

import com.litesuits.orm.db.annotation.Column
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.annotation.Unique
import com.litesuits.orm.db.enums.AssignType
import java.io.Serializable

@Table("type")
data class Type(
        @field:PrimaryKey(AssignType.AUTO_INCREMENT) var id: Long = 0,
        @field:Unique @field:Column("type") var type: String = "",
        @field:Column("sort") var sort: Int = 0,
        @field:Column("visibility") var visibility: Boolean = false
) : Serializable
