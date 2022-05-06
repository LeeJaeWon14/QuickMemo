package com.example.quickmemo.activity.viewmodel

import androidx.lifecycle.ViewModel

class MemoViewModel : ViewModel() {
//    val memoText : MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }

    var memoText: String = ""
        set(value) {
            field = value
        }
        get() = field

    var isUnlock: Boolean = false
}