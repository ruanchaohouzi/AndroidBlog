package com.ruanchao.androidblog.bean

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
        val scopes: List<String>,
        val note: String,
        @SerializedName("client_id")
        val clientId: String,
        @SerializedName("client_secret")
        val clientSecret: String
) {
    companion object {

        fun generate(): LoginRequestModel {
            return LoginRequestModel(
                    scopes = listOf("user", "repo", "gist", "notifications"),
                    note = "com.qingmei2.sample",
                    clientId = "34ee6b922b45261ec97b",
                    clientSecret = "84c742a4bb7da7808f36b7564cdf071ab08a4e2e"
            )
        }
    }
}