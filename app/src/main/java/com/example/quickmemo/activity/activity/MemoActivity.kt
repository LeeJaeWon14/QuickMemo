package com.example.quickmemo.activity.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quickmemo.R
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.activity.viewmodel.MemoViewModel
import com.example.quickmemo.databinding.ActivityMemoBinding

class MemoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMemoBinding
    private lateinit var viewModel : MemoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

//        viewModel.memoText.observe(this, Observer {
//
//        })

        binding.textAreaInformation.addTextChangedListener {
            viewModel.memoText = it.toString()
            Logger.e("LiveData Save >> ${viewModel.memoText}")
        }
        binding.textAreaInformation.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            if(binding.textAreaInformation.lineCount == binding.textAreaInformation.maxLines) {
                Toast.makeText(this, "최대 라인 수에 도달했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fabBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "Save on pause", Toast.LENGTH_SHORT).show()
//        viewModel.memoText.removeObservers(this)
    }

    private fun init() {
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        viewModel.memoText.let {
            Logger.e("LiveData >> $it")
            binding.textAreaInformation.setText(it)
        }
    }
}