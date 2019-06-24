package com.ruanchao.androidblog.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.ruanchao.androidblog.bean.BlogContent


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