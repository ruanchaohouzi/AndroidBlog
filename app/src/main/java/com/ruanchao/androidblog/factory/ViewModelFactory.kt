package com.ruanchao.androidblog.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ruanchao.androidblog.net.NetWorkManager
import com.ruanchao.androidblog.MainApplication
import com.ruanchao.androidblog.db.BlogContentDatabase
import com.ruanchao.androidblog.ui.home.HomeBlogRepo
import com.ruanchao.androidblog.db.UserDatabase
import com.ruanchao.androidblog.ui.home.HomeBlogViewModel
import com.ruanchao.androidblog.ui.knowledge.KnowledgeRepo
import com.ruanchao.androidblog.ui.knowledge.KnowledgeViewModel
import com.ruanchao.androidblog.ui.publicnumber.PublicNumberRepo
import com.ruanchao.androidblog.ui.publicnumber.PublicNumberViewModel
import com.ruanchao.androidblog.ui.login.LoginRepo
import com.ruanchao.androidblog.ui.login.LoginViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor() : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        when{
            //........可以创建更多的ViewModel......

            modelClass.isAssignableFrom(HomeBlogViewModel::class.java) -> {

                val homeBlogRepo = HomeBlogRepo(
                    NetWorkManager.getInstance().getWanAndroidApi(),
                    BlogContentDatabase.getInstance(MainApplication.context!!)!!.blogContentDao()
                )
                val homeBlogViewModel
                        = HomeBlogViewModel(homeBlogRepo)
                return homeBlogViewModel as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val loginRepo = LoginRepo(NetWorkManager.getInstance().getWanAndroidApi(),
                    UserDatabase.getInstance(MainApplication.context)?.userDao())
                return LoginViewModel(loginRepo) as T
            }

            modelClass.isAssignableFrom(PublicNumberViewModel::class.java) ->{
                val repo = PublicNumberRepo(NetWorkManager.getInstance().getWanAndroidApi())
                return PublicNumberViewModel(repo) as T
            }
            modelClass.isAssignableFrom(KnowledgeViewModel::class.java) ->{
                val repo = KnowledgeRepo(NetWorkManager.getInstance().getWanAndroidApi())
                return KnowledgeViewModel(repo) as T
            }

            else ->{
                throw IllegalArgumentException("viewModel is not suitable")
            }
        }
    }

    //最好通过单例模式创建工厂，因为多次用到工厂，google做法
    companion object {
        @Volatile private var mInstance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (mInstance == null){
                synchronized(this){
                    if (mInstance == null){
                        mInstance = ViewModelFactory()
                    }
                }
            }
            return mInstance
        }
    }

}