package com.ruanchao.androidblog.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.databinding.BindingAdapter
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ruanchao.androidblog.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern


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

@BindingAdapter("srcImg")
fun ImageView.srcImg(collected: Boolean){
    val resId = if (collected) R.mipmap.artical_collected else R.mipmap.artical_discollected
    setImageResource(resId)
}

fun <T> MutableLiveData<T>.set(t: T?): MutableLiveData<T>{
    this.postValue(t) // or loading.setValue(false)
    return this
}

fun <T> MutableLiveData<T>.get() = this.value

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(activity, msg, duration).show()
}

fun getDisplayWidth(context: Context): Int {
    var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.getDefaultDisplay().getWidth()
}


/**
 * 判断字符串是否为URL
 * @param urls 用户头像key
 * @return true:是URL、false:不是URL
 */
fun String.isHttpUrl(): Boolean {
    var isurl = false
    val regex =
        "(((https|http)?://)?([a-z0-9]+[.])|(www.))" + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)"//设置正则表达式

    val pat = Pattern.compile(regex.trim { it <= ' ' })//比对
    val mat = pat.matcher(this.trim { it <= ' ' })
    isurl = mat.matches()//判断是否匹配
    if (isurl) {
        isurl = true
    }
    return isurl
}




