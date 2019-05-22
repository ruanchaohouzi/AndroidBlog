package com.ruanchao.mvvmdemo.db

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import com.ruanchao.mvvmdemo.bean.UserInfo
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



@Database(entities = arrayOf(UserInfo::class), version = 1)
abstract class UserDb : RoomDatabase() {

    companion object {
        @Volatile private var mInstance: UserDb? = null

        fun getInstance(context: Context): UserDb?{
            if (mInstance == null){
                synchronized(this){
                    if (mInstance == null){
                        mInstance = buildDatabase(context)
                    }
                }
            }
            return mInstance
        }

        fun buildDatabase(context: Context): UserDb {
            return Room.databaseBuilder(context,UserDb::class.java,"user.db")
                .build()
        }
    }

    abstract fun userDao(): UserDao


}