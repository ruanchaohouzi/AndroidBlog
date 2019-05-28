package com.ruanchao.mvvmdemo.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ruanchao.mvvmdemo.bean.BlogContent

@Dao
interface BlogContentDao {

    @Query("select * from blog_content order by id DESC limit 1")
    fun getBlogContent():MutableList<BlogContent>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateBlogContent(blogContent: BlogContent)
}