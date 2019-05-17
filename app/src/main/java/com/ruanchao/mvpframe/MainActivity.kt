package com.ruanchao.mvpframe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ruanchao.mvpframe.Home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.homeFragmentContent, HomeFragment())
            .commit()

    }
}
