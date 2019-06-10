package com.ruanchao.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ruanchao.mvvmdemo.ui.home.HomeBlogFragment
import com.ruanchao.mvvmdemo.ui.publicnumber.PublicNumberFragment
import com.ruanchao.mvvmdemo.ui.test.TestFragment
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.view.BottomTabLayoutView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var mBottomTabLayoutView: BottomTabLayoutView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        test()

        mBottomTabLayoutView = findViewById<BottomTabLayoutView>(R.id.home_tab_layout)
        val fragments = ArrayList<androidx.fragment.app.Fragment>()
        fragments.add(PublicNumberFragment())

        fragments.add(HomeBlogFragment())

        fragments.add(TestFragment())

        fragments.add(TestFragment())

        fragments.add(TestFragment())

        mBottomTabLayoutView!!.initView(supportFragmentManager, fragments)
    }

    fun test(){
        val create1 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onNext("wahaha")
                emitter.onComplete()
            }

        }).subscribeOn(Schedulers.io())

        val create2 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onError(Throwable("err"))
            }

        }).subscribeOn(Schedulers.io())

        Observable.concat(create1, create2)
            .observeOn(AndroidSchedulers.mainThread(),true)
            .subscribe(object : Observer<String>{
                override fun onComplete() {
                    Log.i("rxjava1111", "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.i("rxjava1111", "onSubscribe")
                }

                override fun onNext(t: String) {
                    Log.i("rxjava1111", "onNext$t")
                }

                override fun onError(e: Throwable) {
                    Log.i("rxjava1111", "onError")
                }

            })
    }





}
