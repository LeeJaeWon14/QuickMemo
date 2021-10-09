package com.example.quickmemo.activity.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quickmemo.R
import com.example.quickmemo.databinding.ActivityMemoBinding

class MemoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding = ActivityMemoBinding.inflate(layoutInflater)

        binding.textAreaInformation.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            if(binding.textAreaInformation.lineCount == binding.textAreaInformation.maxLines) {
                Toast.makeText(this, "최대 라인 수에 도달했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "Save on pause", Toast.LENGTH_SHORT).show()
    }
}