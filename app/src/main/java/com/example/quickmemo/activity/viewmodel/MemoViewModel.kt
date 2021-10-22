package com.example.quickmemo.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemoViewModel : ViewModel() {
    val memoText : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}