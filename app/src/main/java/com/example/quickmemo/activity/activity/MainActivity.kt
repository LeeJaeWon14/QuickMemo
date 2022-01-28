package com.example.quickmemo.activity.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.quickmemo.R
import com.example.quickmemo.activity.adapter.MemoPagerAdatper
import com.example.quickmemo.activity.util.BiometricManager
import com.example.quickmemo.activity.util.Pref
import com.example.quickmemo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val tabTitle = arrayOf("메모", "휴지통")
    var isUnlock = false
    private var isExit = false
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
                    AlertDialog.Builder(this@MainActivity)
                        .setMessage(getString(R.string.str_need_opengraph_app))
                        .setPositiveButton(getString(R.string.str_ok_label)) { _, _ ->
                            startActivity(Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://linksharing.samsungcloud.com/q1UL29jHPqMA")
                            ))
                        }
                        .setNegativeButton(getString(R.string.str_cancel_label), null)
                        .show()
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

            fabFingerprint.setOnClickListener {
                checkPreference()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(isFabOpened) fabAction()
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.adapter = MemoPagerAdatper(this@MainActivity)
        if(!isUnlock) {
            binding.llLockScreen.isVisible = true
            binding.rlContent.isVisible = false
            checkPreference()
        }
        else {
            // When move Activity
            binding.llLockScreen.isVisible = false
            binding.rlContent.isVisible = true
        }
        isUnlock = false
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
    override fun onBackPressed() { // When clicked back button, doing exit method.
        if(System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "한번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show()
        }
        else if(System.currentTimeMillis() - time < 2000) {
            isExit = true
            this.finishAffinity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_option -> {
                isUnlock = true
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
        }
        return true
    }

    private fun checkPreference() {
        Pref.getInstance(this)?.let {
            if(it.getBoolean(Pref.PREF_BIOMETRIC)) {
                BiometricManager.getPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        binding.llLockScreen.isVisible = false
                        binding.rlContent.isVisible = true
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@MainActivity, getString(R.string.str_unlock_to_fingerprint_message), Toast.LENGTH_SHORT).show()
                    }
                }).authenticate(BiometricManager.getPromptInfo())
            }
            else {
                binding.llLockScreen.isVisible = false
                binding.rlContent.isVisible = true
            }
        }
    }
}