package com.ruanchao.mvvmdemo.view

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.ActivityAnimalBinding
import com.ruanchao.mvvmdemo.bean.Animal
import com.ruanchao.mvvmdemo.utils.setImageUrl
import com.ruanchao.mvvmdemo.viewmodel.AnimalViewModel
import kotlinx.android.synthetic.main.activity_animal.*

class AnimalActivity : AppCompatActivity() {

    lateinit var mAnimalBinding: ActivityAnimalBinding

    lateinit var mViewModel: AnimalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //需要替换成如下的setContent
        mAnimalBinding = DataBindingUtil.setContentView<ActivityAnimalBinding>(this, R.layout.activity_animal)
        //联系Viewmodel model
        val animal: Animal = Animal("dog", 0)
        mViewModel = AnimalViewModel(animal)
        mAnimalBinding.vm = mViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.destroy()
    }

}
