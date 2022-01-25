package com.example.quickmemo.activity.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.example.quickmemo.R
import com.example.quickmemo.activity.adapter.MemoPagerAdatper
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
            fabAdd.setOnClickListener {
                fabAction()
            }
            fabMemo.setOnClickListener {
                startActivity(Intent(this@MainActivity, MemoActivity::class.java))
            }
            fabBookmark.setOnClickListener {
                // OpenGraph
                val intent = packageManager.getLaunchIntentForPackage("com.example.opengraphsample")
                intent?.let {
                    startActivity(intent)
                } ?: run {
                    Toast.makeText(this@MainActivity, getString(R.string.str_need_opengraph_app), Toast.LENGTH_SHORT).show()
                }
            }
            viewPager.adapter = MemoPagerAdatper(this@MainActivity)
            TabLayoutMediator(binding.tlTab, binding.viewPager, object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                    tab.text = tabTitle[position]
                }
            }).attach()
            tlTab.setSelectedTabIndicatorColor(getColor(R.color.white))
            tlTab.setBackgroundColor(getColor(R.color.purple_700))
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.toolbar.title = tabTitle[position]
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.adapter = MemoPagerAdatper(this@MainActivity)
    }

    private var isFabOpened = false
    private fun fabAction() {
        if(isFabOpened) {
            ObjectAnimator.ofFloat(binding.fabMemo, "translationX", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabBookmark, "translationX", 0f).apply { start() }
            binding.fabAdd.setImageResource(R.drawable.outline_add_24)
        }
        else {
            ObjectAnimator.ofFloat(binding.fabMemo, "translationX", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabBookmark, "translationX", -400f).apply { start() }
            binding.fabAdd.setImageResource(R.drawable.outline_clear_24)
        }

        isFabOpened = !isFabOpened
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