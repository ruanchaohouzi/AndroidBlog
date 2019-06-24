package com.ruanchao.androidblog.net

import com.ruanchao.androidblog.MainApplication
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import android.util.Log
import com.ruanchao.androidblog.utils.*


class NetWorkManager private constructor(){

    private var mWanAndroidRetrofit: Retrofit? = null
    private var mTucaoRetrofit: Retrofit? = null
    private var mGithubRetrofit: Retrofit? = null
    private val CACHE_MAX_SIZE: Long = 1024*1024*20
    private val TAG = NetWorkManager::class.java.simpleName

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
            .addInterceptor(addCookiesInterceptor())
            .addInterceptor(getCookiesInterceptor())
            .build()

        mWanAndroidRetrofit = buildRetrofit(okHttpClient, Constants.BASE_WAN_ANDROID_URL)
        mTucaoRetrofit = buildRetrofit(okHttpClient, Constants.BASE_TUCAO_API_URL)
        mGithubRetrofit = buildRetrofit(okHttpClient, Constants.BASE_GITHUB_API_URL)
    }

    fun getWanAndroidApi(): WanAndroidApi = mWanAndroidRetrofit!!.create(WanAndroidApi::class.java)

    fun buildRetrofit(okHttpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


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

    /**
     * 获取服务端返回的登录cookie信息
     */
    private fun getCookiesInterceptor() : Interceptor = Interceptor {

        val request = it.request()
        val originalResponse = it.proceed(request)
        val encodedPath = request.url().encodedPath()
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
        val encodedPath = request.url().host()
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

}