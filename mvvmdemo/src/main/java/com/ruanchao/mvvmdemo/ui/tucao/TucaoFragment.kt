package com.ruanchao.mvvmdemo.ui.tucao

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class TucaoFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var textView = TextView(activity)
        textView.setText("开发中。。。")
        return textView
    }

}