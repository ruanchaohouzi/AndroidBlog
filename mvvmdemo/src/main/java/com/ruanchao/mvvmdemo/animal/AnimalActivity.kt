package com.ruanchao.mvvmdemo.animal

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.ActivityAnimalBinding
import com.ruanchao.mvvmdemo.factory.ViewModelFactory

class AnimalActivity : AppCompatActivity() {

    lateinit var mAnimalBinding: ActivityAnimalBinding

    lateinit var mViewModel: AnimalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //需要替换成如下的setContent
        mAnimalBinding = DataBindingUtil.setContentView<ActivityAnimalBinding>(this, R.layout.activity_animal)
        //联系Viewmodel model
//        val animal: Animal = Animal("dog", 0)
//        mViewModel = AnimalViewModel(animal)


        //通过ViewModel进行绑定,通过工厂创建ViewModel，确保旋转屏幕，数据不丢失
        var factory = ViewModelFactory.getInstance()
        mViewModel = ViewModelProviders.of(this,factory).get(AnimalViewModel::class.java)


        mAnimalBinding.vm = mViewModel
        //必须要绑定生命周期，不写没效果
        mAnimalBinding.setLifecycleOwner(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.destroy()
    }

}
