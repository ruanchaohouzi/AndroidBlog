package com.ruanchao.androidblog.db

import androidx.room.*
import com.ruanchao.androidblog.bean.User
import io.reactivex.Single

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("select * from user_info where userId=:userId")
    fun getUserInfoById(userId: Int): Single<User>

    @Query("select * from user_info where username=:userName and password=:pwd")
    fun getUserInfo(userName: String, pwd: String): User?

    @Query("select * from user_info order by userId desc limit 0,1;")
    fun getUserInfo():Single<User>


}