package com.ruanchao.mvvmdemo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ruanchao.mvvmdemo.bean.BlogContent


@Database(entities = [BlogContent::class], version = 1)
abstract class BlogContentDatabase: RoomDatabase() {
    companion object {
        @Volatile private var mInstance: BlogContentDatabase? = null

        fun getInstance(context: Context): BlogContentDatabase? {
            if(mInstance == null){
                synchronized(this){
                    if (mInstance == null) {
                        mInstance = Room.databaseBuilder(context, BlogContentDatabase::class.java, "blog.db")
                            .build()
                    }
                }
            }
            return mInstance
        }
    }

    abstract fun blogContentDao(): BlogContentDao
}