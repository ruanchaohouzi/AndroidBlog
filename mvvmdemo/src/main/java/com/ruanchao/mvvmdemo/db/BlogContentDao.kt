package com.ruanchao.mvvmdemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruanchao.mvvmdemo.bean.BlogContent

@Dao
interface BlogContentDao {

    @Query("select * from blog_content order by id DESC limit 1")
    fun getBlogContent():MutableList<BlogContent>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdateBlogContent(blogContent: BlogContent)
}