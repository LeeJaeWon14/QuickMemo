package com.example.quickmemo.activity.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.R
import com.example.quickmemo.activity.activity.MemoActivity
import com.example.quickmemo.activity.room.MemoEntity
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.util.Logger
import com.example.quickmemo.activity.util.MyDateUtil
import kotlin.random.Random

class MemoListAdapter(private val entityList: List<MemoEntity>) : RecyclerView.Adapter<MemoListAdapter.MemoListHolder>() {
    private lateinit var context : Context
    private val memoList = entityList.toMutableList()
    private var size : Int = memoList.size
    class MemoListHolder(view : View) : RecyclerView.ViewHolder(view) {
        val date : TextView = view.findViewById(R.id.tv_lastDate)
        val memo : TextView = view.findViewById(R.id.tv_memo)
        val card : CardView = view.findViewById(R.id.cv_memoCard)
        var remove : ImageView = view.findViewById(R.id.iv_remove_memo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        context = parent.context
        return MemoListHolder(view)
    }

    override fun onBindViewHolder(holder: MemoListHolder, position: Int) {
        memoList.let {
            holder.apply {
                val entity = it[position]
                card.setBackgroundColor(ContextCompat.getColor(context, randomColor()))
                date.text = entity.last_date
                memo.text = entity.memo
                Logger.i("entitiy name is ${entity.memo}")
                remove.setOnClickListener {
                    if(memoList.size == 1) return@setOnClickListener
                    Toast.makeText(context, "Yet", Toast.LENGTH_SHORT).show()
                    memoList.removeAt(position)
                    MemoRoomDatabase.getInstance(context).getMemoDAO()
                        .deleteMemo(entity)
                    size = memoList.size
                    notifyDataSetChanged()
                }
                card.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable("entity", entity)
                    context.startActivity(Intent(context, MemoActivity::class.java).putExtra("entityBundle", bundle))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    private fun randomColor() : Int {
        return when(Random.nextInt(3)) {
            0 -> R.color.purple_700
            1 -> R.color.purple_500
            2 -> R.color.purple_200
            else -> 0
        }
    }
}