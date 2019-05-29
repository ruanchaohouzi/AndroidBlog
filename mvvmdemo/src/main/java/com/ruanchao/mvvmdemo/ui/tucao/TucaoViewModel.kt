package com.ruanchao.mvvmdemo.ui.tucao

import android.arch.lifecycle.ViewModel
import com.ruanchao.mvvmdemo.utils.schedule
import io.reactivex.Observable
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

class TucaoViewModel(private val tucaoRepo: TucaoRepo):ViewModel(){

    fun loadData(){
        val maxRetries: Int = 3
        val delayMillis: Long = 2000L
        var retryCount:Int = 0
        tucaoRepo.index()
            .schedule()
                //失败重传三次
            .retryWhen {
                it.flatMap {
                    if (++retryCount < maxRetries){
                        Observable.timer(delayMillis, TimeUnit.MILLISECONDS)
                    }else{
                        Observable.error(it)
                    }
                }
            }.map {
                val parse = Jsoup.parse(it.string())
                parse
            }
    }
}