package com.ruanchao.mvvmdemo.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ruanchao.mvvmdemo.factory.ViewModelFactory

fun <T : ViewModel> obtainViewModel(fragment:Fragment, modelClass:Class<T>)=
    ViewModelProviders.of(fragment,ViewModelFactory.getInstance()).get(modelClass)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(modelClass:Class<T>)=
    ViewModelProviders.of(this,ViewModelFactory.getInstance()).get(modelClass)