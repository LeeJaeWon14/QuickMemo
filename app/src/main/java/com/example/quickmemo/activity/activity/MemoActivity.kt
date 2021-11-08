package com.example.quickmemo.activity.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.quickmemo.activity.room.MemoEntity
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.activity.util.MyDateUtil
import com.example.quickmemo.activity.viewmodel.MemoViewModel
import com.example.quickmemo.databinding.ActivityMemoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMemoBinding
    private lateinit var viewModel : MemoViewModel
    private var sharedString : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        actionBar?.hide()
        setSupportActionBar(binding.tbMemo)

        intent.getBundleExtra("entityBundle")?.let {
            val entity = it.getSerializable("entity") as MemoEntity
            binding.textAreaInformation.setText(entity.memo)
        }

        when(intent.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    sharedString = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                    binding.textAreaInformation.setText(sharedString)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun init() {
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
        viewModel.memoText.let {
            Logger.e("LiveData >> $it")
            binding.textAreaInformation.setText(it)
        }

        binding.apply {
            textAreaInformation.addTextChangedListener {
                viewModel.memoText = it.toString()
                Logger.e("LiveData Save >> ${viewModel.memoText}")
            }

            textAreaInformation.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
                if(binding.textAreaInformation.lineCount == binding.textAreaInformation.maxLines) {
                    Toast.makeText(this@MemoActivity, "최대 라인 수에 도달했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            fabBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            MemoRoomDatabase.getInstance(this@MemoActivity).getMemoDAO()
                .insertMemo(MemoEntity(
                    MyDateUtil.getDate(MyDateUtil.HANGUEL),
                    viewModel.memoText,
                    0
                ))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(viewModel.memoText.isNotEmpty()) {
            save()
            Toast.makeText(this, "Save on phone", Toast.LENGTH_SHORT).show()
        }
    }
}