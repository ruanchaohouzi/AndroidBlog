package com.ruanchao.mvvmdemo.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "blog_content")
class BlogContent(){

    constructor(id: Int, content: String) : this() {
        this.id = id
        this.content = content
    }

    @PrimaryKey
    var id: Int = 0;

    var content: String? = null
}