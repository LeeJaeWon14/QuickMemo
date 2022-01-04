package com.example.quickmemo.activity.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.quickmemo.R
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.room.entity.MemoEntity
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
    private var entity : MemoEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        actionBar?.hide()

        intent.getBundleExtra("entityBundle")?.let {
            entity = it.getSerializable("entity") as MemoEntity
            binding.textAreaInformation.setText(entity?.memo)
            setUpFocus()
        }

        when(intent.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    sharedString = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                    binding.textAreaInformation.setText(sharedString)
                }
            }
        }

        //keyboard up
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(binding.textAreaInformation, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun init() {
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
//        viewModel.memoText.observe(this, Observer {
//            binding.textAreaInformation.setText(it)
//        })

        binding.apply {
            textAreaInformation.addTextChangedListener {
                viewModel.memoText = it.toString()
            }

            textAreaInformation.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
                if(binding.textAreaInformation.lineCount == binding.textAreaInformation.maxLines -1) {
                    Toast.makeText(this@MemoActivity, getString(R.string.str_text_line_max), Toast.LENGTH_SHORT).show()
                }
            }
            setUpFocus()

            fabBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            MemoRoomDatabase.getInstance(this@MemoActivity).getMemoDAO()
                .insertMemo(
                    MemoEntity(
                    MyDateUtil.getDate(MyDateUtil.HANGUEL),
                    viewModel.memoText,
                    0
                ))
        }
    }

    private fun update(entity: MemoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            MemoRoomDatabase.getInstance(this@MemoActivity).getMemoDAO()
                .updateMemo(entity)
        }
    }

    private fun existPrevMemo(text: String) : Boolean = entity?.memo == text

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel.memoText.isNotEmpty() && !existPrevMemo(viewModel.memoText)) {
            if(entity == null) {
                save()
                Toast.makeText(this, "Save on phone", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show()
                entity?.let { update(it) }
            }
        }
    }

    private fun setUpFocus() {
        // focus move to editText
        binding.textAreaInformation.requestFocus()
        binding.textAreaInformation.setSelection(viewModel.memoText.length)
    }
}