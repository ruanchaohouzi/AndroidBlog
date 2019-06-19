package com.ruanchao.mvvmdemo.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
class User{

    @PrimaryKey(autoGenerate = true)
    var userId : Int = 0
    var admin: Boolean = false
    var email: String? = null
    var icon: String? = null
    var id: Int = -1
    var password: String? = null
    var token: String? = null
    var type: Int = 0
    var username: String? = null

}