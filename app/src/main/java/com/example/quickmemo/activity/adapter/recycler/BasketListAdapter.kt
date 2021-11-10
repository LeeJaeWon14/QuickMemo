package com.example.quickmemo.activity.adapter.recycler

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quickmemo.R
import com.example.quickmemo.activity.activity.MemoActivity
import com.example.quickmemo.activity.room.MemoRoomDatabase
import com.example.quickmemo.activity.room.entity.BasketEntity
import com.example.quickmemo.activity.room.entity.MemoEntity
import kotlin.random.Random

class BasketListAdapter(private var context : Context) : RecyclerView.Adapter<BasketListAdapter.MemoListHolder>() {
    private val memoList = MemoRoomDatabase.getInstance(context).getBasketDAO().getBasketList().toMutableList()
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
                card.apply {
//                    setBackgroundColor(ContextCompat.getColor(context, randomColor()))
                    setBackgroundResource(randomColor())
                    setOnClickListener {
                        val dlg = AlertDialog.Builder(context)
                        dlg.setMessage(context.getString(R.string.str_restore_message))
                        dlg.setPositiveButton("복구") { _: DialogInterface, _: Int ->
                            // restore
                            restoreMemo(entity, position)
                        }
                        dlg.setNegativeButton("취소", null)
                        dlg.setCancelable(false)
                        dlg.show()
                    }
                    setOnLongClickListener {
                        val popup = PopupMenu(context, it)
                        popup.setOnMenuItemClickListener {
                            when(it.itemId) {
                                R.id.menu_memo_resotre -> {
                                    restoreMemo(entity, position)
                                }
                                else -> { return@setOnMenuItemClickListener false }
                            }
                            return@setOnMenuItemClickListener true
                        }
                        popup.inflate(R.menu.menu_basket)
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

    private fun removeMemo(entity: BasketEntity, position: Int) {
        memoList.removeAt(position)
        MemoRoomDatabase.getInstance(context).getBasketDAO()
            .deleteBasket(entity)
        size = memoList.size
        notifyDataSetChanged()
    }

    private fun restoreMemo(entity: BasketEntity, position: Int) {
        removeMemo(entity, position)
        MemoRoomDatabase.getInstance(context).getMemoDAO()
            .insertMemo(
                MemoEntity(
                    entity.last_date,
                    entity.memo,
                    0
                )
            )
    }
}