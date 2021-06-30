package com.ruanchao.androidblog.net2

import com.ruanchao.androidblog.MainApplication
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import android.util.Log
import com.ruanchao.androidblog.utils.*


class NetWorkManager2 private constructor(){

    private var mWanAndroidRetrofit: Retrofit? = null
    private val TAG = NetWorkManager2::class.java.simpleName

    init {
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(MainApplication.context!!.cacheDir, "okhttpcache")
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(addCookiesInterceptor())
            .addInterceptor(getCookiesInterceptor())
            .build()
        mWanAndroidRetrofit = buildRetrofit(okHttpClient, Constants.BASE_WAN_ANDROID_URL)
    }

    fun getLoginApi(): LoginApi = mWanAndroidRetrofit!!.create(LoginApi::class.java)

    private fun buildRetrofit(okHttpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    /**
     * 获取服务端返回的登录cookie信息
     */
    private fun getCookiesInterceptor() : Interceptor = Interceptor {

        val request = it.request()
        val originalResponse = it.proceed(request)
        val encodedPath = request.url.encodedPath
        if ((encodedPath.contains("user/register")
            || encodedPath.contains("user/login"))
            && !originalResponse.headers("Set-Cookie").isEmpty()){
            val cookies = mutableSetOf<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
                Log.i(TAG, "cookie: $header")
            }
            //保存cookie信息
            PreferencesUtil.saveSetString(PreferencesUtil.COOKIE_KEY, cookies)
        }

        originalResponse
    }

    private fun addCookiesInterceptor() : Interceptor = Interceptor {

        val request = it.request()
        val builder = request.newBuilder()
        val encodedPath = request.url.host
        Log.i(TAG,"encodedPath:$encodedPath")
        if (encodedPath.contains("wanandroid.com")) {
            PreferencesUtil.getSet(PreferencesUtil.COOKIE_KEY)?.let {
                for (cookie in it) {
                    builder.addHeader("Cookie", cookie)
                    // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                    Log.i(TAG, "Adding Header: $cookie")
                }
            }
        }
        it.proceed(builder.build())
    }

    companion object {

        private var mNetWorkManager2: NetWorkManager2? = null

        fun getInstance(): NetWorkManager2 {
            if (mNetWorkManager2 == null){
                synchronized(this){
                    if (mNetWorkManager2 == null){
                        mNetWorkManager2 = NetWorkManager2()
                    }
                }
            }
            return mNetWorkManager2!!
        }
    }

}