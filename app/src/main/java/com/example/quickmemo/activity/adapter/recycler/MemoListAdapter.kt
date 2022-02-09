package com.example.quickmemo.activity.adapter.recycler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.R
import com.example.quickmemo.activity.activity.MainActivity
import com.example.quickmemo.activity.activity.MemoActivity
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.room.entity.BasketEntity
import com.example.quickmemo.activity.room.entity.MemoEntity
import kotlin.random.Random

class MemoListAdapter(private var memoList : MutableList<MemoEntity>?) : RecyclerView.Adapter<MemoListAdapter.MemoListHolder>() {
//    private var memoList = MemoRoomDatabase.getInstance(context).getMemoDAO().getMemoList().toMutableList()
    private var size : Int = memoList?.size ?: 0
    private lateinit var context: Context

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
        memoList?.let {
            holder.apply {
                val entity = it[position]
                card.apply {
                    setBackgroundResource(randomColor())
                    setOnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable("entity", entity)
                        context.startActivity(Intent(context, MemoActivity::class.java).putExtra("entityBundle", bundle))
//                        (context as MainActivity).isUnlock = true
                    }
                    setOnLongClickListener {
                        val popup = PopupMenu(context, it)
                        popup.setOnMenuItemClickListener {
                            when(it.itemId) {
                                R.id.menu_memo_share -> {
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, "${entity.memo} \r\n ---------- \r\nby. ${context.getString(R.string.app_name)}")
                                    context.startActivity(Intent.createChooser(intent, "Shared memo"))
                                }
                                R.id.menu_memo_remove -> {
                                    removeMemo(entity, position)
                                }
                                else -> { return@setOnMenuItemClickListener false }
                            }
                            return@setOnMenuItemClickListener true
                        }
                        popup.inflate(R.menu.menu_memo_card)
                        popup.show()

                        true
                    }
                }
                date.text = entity.last_date
                memo.text = entity.memo
                remove.setOnClickListener {
                    removeMemo(entity, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    private fun randomColor() : Int {
        return when(Random.nextInt(3)) {
            0 -> R.drawable.selector_card_700
            1 -> R.drawable.selector_card_500
            2 -> R.drawable.selector_card_200
            else -> 0
        }
    }

    private fun removeMemo(entity: MemoEntity, position: Int) {
//        if(memoList.size == 1) return
        memoList?.let {
            it.removeAt(position)
            MemoRoomDatabase.getInstance(context).getMemoDAO()
                .deleteMemo(entity)
            MemoRoomDatabase.getInstance(context).getBasketDAO()
                .insertBasket(
                    BasketEntity(
                        entity.last_date,
                        entity.memo,
                        0
                    )
                )
            size = it.size
            notifyDataSetChanged()

        }
        if(size < 1) {
            (context as MainActivity).updatePager(0)
        }
    }
}