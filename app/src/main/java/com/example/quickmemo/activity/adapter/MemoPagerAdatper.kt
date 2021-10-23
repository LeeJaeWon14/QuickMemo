package com.example.quickmemo.activity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quickmemo.activity.fragment.MemoListFragment

class MemoPagerAdatper(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return MemoListFragment.newInstance(position)
    }
}