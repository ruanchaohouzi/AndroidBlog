package com.ruanchao.androidblog.bean

data class Projects(
    val curPage: Int,
    val datas: MutableList<DataInfo>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Tag(
    val name: String,
    val url: String
)