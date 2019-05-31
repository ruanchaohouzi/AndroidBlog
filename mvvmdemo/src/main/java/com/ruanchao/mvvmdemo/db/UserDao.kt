package com.ruanchao.mvvmdemo.db

import android.arch.persistence.room.*
import com.ruanchao.mvvmdemo.bean.UserInfo1
import io.reactivex.Single

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserInfo1>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserInfo1)

    @Query("select * from user_info where userId=:userId")
    fun getUserInfoById(userId: Int): Single<UserInfo1>

    @Query("select * from user_info where userName=:userName and pwd=:pwd")
    fun getUserInfo(userName: String, pwd: String): UserInfo1?

    @Delete()
    fun deleteByUser(user: UserInfo1)


}