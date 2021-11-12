package com.example.quickmemo.activity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quickmemo.activity.room.dao.BasketDAO
import com.example.quickmemo.activity.room.dao.MemoDAO
import com.example.quickmemo.activity.room.entity.BasketEntity
import com.example.quickmemo.activity.room.entity.MemoEntity

@Database(entities = [MemoEntity::class, BasketEntity::class], version = 2, exportSchema = true)
abstract class MemoRoomDatabase : RoomDatabase() {
    abstract fun getMemoDAO() : MemoDAO
    abstract fun getBasketDAO() : BasketDAO

    companion object {
        private var instance : MemoRoomDatabase? = null

        fun getInstance(context : Context) : MemoRoomDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, MemoRoomDatabase::class.java, "memoRoom.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}