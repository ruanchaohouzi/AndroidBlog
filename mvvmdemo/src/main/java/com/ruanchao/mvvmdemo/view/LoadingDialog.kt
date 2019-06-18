package com.ruanchao.mvvmdemo.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ruanchao.mvvmdemo.R
import android.view.WindowManager
import android.view.Gravity
import com.ruanchao.mvvmdemo.utils.getDisplayWidth


class LoadingDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.loading_dialog_layout, container, false)

        //Do something
        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog?.getWindow()
       // 设置了窗口的背景色为透明，这一步是必须的；
        window!!.setBackgroundDrawableResource(R.color.transparent)
        //设置了窗口的 Pading 值全部为0，这一步也是必须的，内容不能填充全部宽度和高度。
        window.decorView.setPadding(0, 5, 0, 5)
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = getDisplayWidth(activity as Context) - 100
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        dialog?.setCanceledOnTouchOutside(false)

        return view
    }
}