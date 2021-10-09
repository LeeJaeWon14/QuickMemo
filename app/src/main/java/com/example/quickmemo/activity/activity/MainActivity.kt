package com.example.quickmemo.activity.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.quickmemo.R
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab
            .setOnClickListener {
                Snackbar.make(it, "Test", Snackbar.LENGTH_SHORT)
                    .setAction("OK", null)
                    .show()
            }

        val memoDAO = MemoRoomDatabase.getInstance(this).getMemoDAO()
    }

    private fun initList() {
        
    }
}