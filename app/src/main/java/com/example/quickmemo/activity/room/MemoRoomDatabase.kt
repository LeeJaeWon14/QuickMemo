package com.example.quickmemo.activity.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoEntity::class], version = 1, exportSchema = true)
abstract class MemoRoomDatabase : RoomDatabase() {
    abstract fun getMemoDAO() : MemoDAO

    companion object {
        private var instance : MemoRoomDatabase? = null

        fun getInstance(context : Context) : MemoRoomDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, MemoRoomDatabase::class.java, "memoRoom.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}