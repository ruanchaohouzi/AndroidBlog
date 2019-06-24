package com.ruanchao.androidblog.bean

class UserInfo{

    var user:User? = null

    companion object {

        @Volatile private var  mInstance: UserInfo? = null
        fun getInstance(): UserInfo? {
            if (mInstance == null){
                synchronized(UserInfo::class.java){
                    if (mInstance == null){
                        mInstance = UserInfo()
                    }
                }
            }
            return mInstance
        }
    }
}

////Java实现
//public class SingletonDemo {
//    private volatile static SingletonDemo instance;
//    private SingletonDemo(){}
//    public static SingletonDemo getInstance(){
//        if(instance==null){
//            synchronized (SingletonDemo.class){
//                if(instance==null){
//                    instance=new SingletonDemo();
//                }
//            }
//        }
//        return instance;
//    }
//}
