package com.ruanchao.androidblog.db

import androidx.room.*
import android.content.Context
import com.ruanchao.androidblog.bean.User


@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    companion object {
        @Volatile private var mInstance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase?{
            if (mInstance == null){
                synchronized(this){
                    if (mInstance == null){
                        mInstance = buildDatabase(context)
                    }
                }
            }
            return mInstance
        }

        private fun buildDatabase(context: Context): UserDatabase {
            return Room.databaseBuilder(context,UserDatabase::class.java,"user.db")
                .build()
        }
    }

    abstract fun userDao(): UserDao


}