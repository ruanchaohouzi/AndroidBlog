package com.ruanchao.mvpframe.net

import com.ruanchao.mvpframe.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetWorkManager private constructor(){

    var mRetrofit: Retrofit? = null;

    companion object {
        var mNetWorkManager: NetWorkManager? = null
        fun getInstance(): NetWorkManager {

            if (mNetWorkManager == null){
                synchronized(this){
                    if (mNetWorkManager == null){
                        mNetWorkManager = NetWorkManager()
                    }
                }
            }
            return mNetWorkManager!!;
        }
    }

    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        mRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    fun getRequestApi(): RequestApi = mRetrofit!!.create(RequestApi::class.java)

}