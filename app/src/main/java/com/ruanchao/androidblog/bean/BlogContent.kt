package com.ruanchao.androidblog.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_content")
class BlogContent(){

    constructor(id: Int, content: String) : this() {
        this.id = id
        this.content = content
    }

    @PrimaryKey
    var id: Int = 0

    var content: String? = null
}