package com.example.quickmemo.activity.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.quickmemo.R
import com.example.quickmemo.activity.adapter.MemoPagerAdatper
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()

        val tabTitle = arrayOf("메모", "휴지통")
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = tabTitle[0]
        binding.toolbar.setTitleTextColor(getColor(R.color.white))

        binding.fab.setOnClickListener {
            Toast.makeText(this, "New Memo", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MemoActivity::class.java))
        }
//         val memoDAO = MemoRoomDatabase.getInstance(this).getMemoDAO()


        binding.viewPager.adapter = MemoPagerAdatper(this)
        TabLayoutMediator(binding.tlTab, binding.viewPager, object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {

            }
        }).attach()
        binding.tlTab.setSelectedTabIndicatorColor(getColor(R.color.white))
        binding.tlTab.setBackgroundColor(getColor(R.color.purple_700))
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.toolbar.title = tabTitle[position]
            }
        })
    }

    private var time : Long = 0
    override fun onBackPressed() { //뒤로가기 클릭 시 종료 메소드
        if(System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "한번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show()
        }
        else if(System.currentTimeMillis() - time < 2000) {
            this.finishAffinity()
        }
    }
}