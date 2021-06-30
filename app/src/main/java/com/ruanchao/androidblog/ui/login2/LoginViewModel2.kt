package com.ruanchao.androidblog.ui.login2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ruanchao.androidblog.MainApplication
import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.User
import com.ruanchao.androidblog.bean.UserInfo
import com.ruanchao.androidblog.db.UserDao
import com.ruanchao.androidblog.db.UserDatabase
import com.ruanchao.androidblog.event.LoginUserMsg
import com.ruanchao.androidblog.net2.NetWorkManager2
import com.ruanchao.androidblog.utils.PreferencesUtil
import com.ruanchao.androidblog.utils.schedule
import com.ruanchao.androidblog.utils.set
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus


class LoginViewModel2(): ViewModel() {

    private val repo: LoginRepo by lazy { LoginRepo(NetWorkManager2.getInstance().getLoginApi(),
        UserDatabase.getInstance(MainApplication.context)?.userDao()) }

    var userInfoData = MutableLiveData<User>()

    var loginError = MutableLiveData<String>()

    var isLoading = MutableLiveData<Boolean>()

    val TAG = LoginViewModel2::class.java.simpleName


    fun login(userName: String, pwd: String) {
        viewModelScope.launch {
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
                loginError.set("用户名或者密码不能为空")
                return@launch
            }
            isLoading.postValue(true)
            var user: User? = null
            withContext(Dispatchers.IO) {
                try {
                    delay(5000L)
                    val userResult = repo.login(userName, pwd)
                    if (userResult.data != null) {
                        user = userResult.data
                        userInfoData.postValue(user)
                    } else {
                        loginError.postValue(userResult.errorMsg)
                    }
                    isLoading.postValue(false)
                } catch (e: Exception) {
                    loginError.set("登录异常")
                    isLoading.postValue(false)
                }
            }
        }
    }


}