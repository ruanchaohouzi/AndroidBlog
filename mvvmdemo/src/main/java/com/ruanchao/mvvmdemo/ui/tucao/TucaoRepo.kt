package com.ruanchao.mvvmdemo.ui.tucao

import com.ruanchao.mvvmdemo.net.TucaoApi
import io.reactivex.Observable
import okhttp3.ResponseBody

class TucaoRepo(private val remote: TucaoApi){

    fun index() : Observable<ResponseBody> = remote.index()

}