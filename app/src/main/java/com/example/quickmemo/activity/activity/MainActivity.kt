package com.example.quickmemo.activity.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.quickmemo.R
import com.example.quickmemo.activity.adapter.MemoPagerAdatper
import com.example.quickmemo.activity.fragment.MemoListFragment
import com.example.quickmemo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val tabTitle = arrayOf("메모", "휴지통")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()


        setSupportActionBar(binding.toolbar)
        bindingInit()
    }

    private fun bindingInit() {
        binding.apply {
            toolbar.title = tabTitle[0]
            toolbar.setTitleTextColor(getColor(R.color.white))
            fab.setOnClickListener {
                Toast.makeText(this@MainActivity, "New Memo", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, MemoActivity::class.java))
            }
            viewPager.adapter = MemoPagerAdatper(this@MainActivity)
            TabLayoutMediator(binding.tlTab, binding.viewPager, object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {

                }
            }).attach()
            tlTab.setSelectedTabIndicatorColor(getColor(R.color.white))
            tlTab.setBackgroundColor(getColor(R.color.purple_700))
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.toolbar.title = tabTitle[position]
//                    if(supportFragmentManager.fragments.isNotEmpty()) {
//                        MemoListFragment.newInstance(position).dataChanged()
//                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.adapter = MemoPagerAdatper(this@MainActivity)
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