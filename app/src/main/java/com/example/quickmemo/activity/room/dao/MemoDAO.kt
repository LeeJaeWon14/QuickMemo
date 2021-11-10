package com.example.quickmemo.activity.room.dao

import androidx.room.*
import com.example.quickmemo.activity.room.entity.MemoEntity

@Dao
interface MemoDAO {
    @Query("SELECT * FROM MemoEntity")
    fun getMemoList() : List<MemoEntity>

    @Insert
    fun insertMemo(entity: MemoEntity)

    @Update
    fun updateMemo(entity: MemoEntity)

    @Delete
    fun deleteMemo(entity: MemoEntity)
}