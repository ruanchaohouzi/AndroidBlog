package com.ruanchao.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ruanchao.mvvmdemo.databinding.ActivityMainBinding
import com.ruanchao.mvvmdemo.ui.home.HomeBlogFragment
import com.ruanchao.mvvmdemo.ui.knowledge.KnowledgeFragment
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


        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        supportActionBar?.hide()

        mBottomTabLayoutView = findViewById<BottomTabLayoutView>(R.id.home_tab_layout)
        val fragments = ArrayList<androidx.fragment.app.Fragment>()
        fragments.add(PublicNumberFragment())

        fragments.add(HomeBlogFragment())

        fragments.add(KnowledgeFragment())

        fragments.add(TestFragment())

        fragments.add(TestFragment())

        mBottomTabLayoutView!!.initView(supportFragmentManager, fragments)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt("test",1212)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val value = savedInstanceState?.getInt("test")
        Log.i("MainActivity", "test:$value")
        super.onRestoreInstanceState(savedInstanceState)
    }
}
