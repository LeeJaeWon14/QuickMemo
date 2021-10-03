package com.example.quickmemo.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.R

class MemoListAdapter : RecyclerView.Adapter<MemoListAdapter.MemoListHolder>() {
    val size = 1
    class MemoListHolder(view : View) : RecyclerView.ViewHolder(view) {
        val date : TextView = view.findViewById(R.id.tv_lastDate)
        val memo : TextView = view.findViewById(R.id.tv_memo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MemoListHolder(view)
    }

    override fun onBindViewHolder(holder: MemoListHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return size
    }
}