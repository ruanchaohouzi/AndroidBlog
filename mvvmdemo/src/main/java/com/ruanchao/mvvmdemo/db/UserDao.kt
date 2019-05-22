package com.ruanchao.mvvmdemo.db

import android.arch.persistence.room.*
import com.ruanchao.mvvmdemo.bean.UserInfo
import io.reactivex.Single

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserInfo);

    @Query("select * from user_info where userId=:userId")
    fun getUserInfoById(userId: Int): Single<UserInfo>

    @Delete()
    fun deleteByUser(user: UserInfo)


}