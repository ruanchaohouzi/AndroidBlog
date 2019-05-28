package com.ruanchao.mvvmdemo.net

import com.ruanchao.mvvmdemo.utils.Constants
import com.ruanchao.mvvmdemo.utils.NetworkUtil
import com.ruanchao.mvvmdemo.MainApplication
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class NetWorkManager private constructor(){

    private var mRetrofit: Retrofit? = null
    private val CACHE_MAX_SIZE: Long = 1024*1024*20

    companion object {
        private var mNetWorkManager: NetWorkManager? = null
        fun getInstance(): NetWorkManager {

            if (mNetWorkManager == null){
                synchronized(this){
                    if (mNetWorkManager == null){
                        mNetWorkManager = NetWorkManager()
                    }
                }
            }
            return mNetWorkManager!!
        }
    }

    init {
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(MainApplication.context!!.cacheDir, "okhttpcache")
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                //必须添加Network网络拦截器和cache配合使用
//            .addNetworkInterceptor(getCacheInterceptor())
//            .cache(Cache(cacheFile, CACHE_MAX_SIZE))
            .build()

        mRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRequestApi(): RequestApi = mRetrofit!!.create(RequestApi::class.java)

    fun getCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val responseBuild: Response
            if (NetworkUtil.isNetworkAvailable(MainApplication.context!!)) {

                val maxAge = 60 * 60 * 24 // 有网络的时候从缓存1天后失效
                responseBuild = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28 // // 无网络缓存保存四周
                responseBuild = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
            responseBuild

        }
    }

}