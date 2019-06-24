package com.ruanchao.androidblog.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.ruanchao.androidblog.factory.ViewModelFactory

fun <T : ViewModel> obtainViewModel(fragment: Fragment, modelClass:Class<T>)=
    ViewModelProviders.of(fragment,ViewModelFactory.getInstance()).get(modelClass)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(modelClass:Class<T>)=
    ViewModelProviders.of(this,ViewModelFactory.getInstance()).get(modelClass)