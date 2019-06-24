package com.ruanchao.androidblog.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ruanchao.androidblog.databinding.AddCollectionDialogLayoutBinding
import com.ruanchao.androidblog.ui.collection.CollectionViewModel
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.utils.toast


class AddCollectionDialog : DialogFragment() {

    var viewBinding: AddCollectionDialogLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewBinding = AddCollectionDialogLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = (activity as AppCompatActivity).obtainViewModel(CollectionViewModel::class.java)
                lifecycleOwner = this@AddCollectionDialog
            }

        //Do something
        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog?.getWindow()
       // 设置了窗口的背景色为透明，这一步是必须的；
//        window!!.setBackgroundDrawableResource(R.color.transparent)
        //设置了窗口的 Pading 值全部为0，这一步也是必须的，内容不能填充全部宽度和高度。
//        window!!.decorView.setPadding(0, 5, 0, 5)
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
//        wlp.width = getDisplayWidth(activity as Context) - 100
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog?.setCanceledOnTouchOutside(false)
        return viewBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewBinding?.viewModel?.collectErrInfo?.observe(this, Observer {
            it?.let {
                toast(it)
            }
        })
    }
}