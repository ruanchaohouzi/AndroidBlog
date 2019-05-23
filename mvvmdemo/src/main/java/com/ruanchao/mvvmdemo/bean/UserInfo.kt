package com.ruanchao.mvvmdemo.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_info")
class UserInfo{

    @PrimaryKey
    var userId:Int =0

    //可以修改数据库表的列名，采取 @ColumnInfo(name = "userId")

    var userName:String? = null

    var pwd:String? = null
}