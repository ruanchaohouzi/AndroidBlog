package com.ruanchao.mvvmdemo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.ruanchao.mvvmdemo.factory.ViewModelFactory

fun <T : ViewModel> obtainViewModel(fragment: androidx.fragment.app.Fragment, modelClass:Class<T>)=
    ViewModelProviders.of(fragment,ViewModelFactory.getInstance()).get(modelClass)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(modelClass:Class<T>)=
    ViewModelProviders.of(this,ViewModelFactory.getInstance()).get(modelClass)