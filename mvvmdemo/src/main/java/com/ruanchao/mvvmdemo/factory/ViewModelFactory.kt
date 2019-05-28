package com.ruanchao.mvvmdemo.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ruanchao.mvvmdemo.net.NetWorkManager
import com.ruanchao.mvvmdemo.MainApplication
import com.ruanchao.mvvmdemo.bean.Animal
import com.ruanchao.mvvmdemo.db.BlogContentDatabase
import com.ruanchao.mvvmdemo.home.HomeBlogRepo
import com.ruanchao.mvvmdemo.animal.AnimalViewModel
import com.ruanchao.mvvmdemo.home.HomeBlogViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor() : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        when{
            modelClass.isAssignableFrom(AnimalViewModel::class.java) -> {
                val animal = Animal("dog", 0)
                val viewModel = AnimalViewModel(animal)
                return viewModel as T
            }
            //........可以创建更多的ViewModel......

            modelClass.isAssignableFrom(HomeBlogViewModel::class.java) -> {

                val homeBlogRepo = HomeBlogRepo(
                    NetWorkManager.getInstance().getRequestApi(),
                    BlogContentDatabase.getInstance(MainApplication.context!!)!!.blogContentDao()
                )
                val homeBlogViewModel
                        = HomeBlogViewModel(homeBlogRepo)
                return homeBlogViewModel as T
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