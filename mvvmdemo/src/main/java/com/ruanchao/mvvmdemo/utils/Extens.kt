package com.ruanchao.mvvmdemo.utils

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.schedule(): Observable<T>{
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?){
    Glide.with(context).load(url).into(this)
}

//@BindingAdapter("visibleOrGone")
//fun View.setVisibleOrGone(visible: Boolean){
//    this.visibility = if (visible) View.VISIBLE else View.GONE
//}

//给view增加扩展函数属性
@BindingAdapter("visible")
fun View.setVisible(visible: Boolean){
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

//如果view不能进行绑定，这里需要自定义一下BindingAdapter
@BindingAdapter("toast")
fun View.bindToast(msg:Throwable?){
    msg?.let {
        Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
    }
}

@set:BindingAdapter("visibleOrGone")
var View.VisibleOrGone
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun <T> MutableLiveData<T>.set(t: T?): MutableLiveData<T>{
    this.postValue(t) // or loading.setValue(false)
    return this
}

fun <T> MutableLiveData<T>.get() = this.value




