package com.example.quickmemo.activity.room

import androidx.room.*

@Dao
interface MemoDAO {
    @Query("SELECT * FROM MemoEntity")
    fun getMemoList() : List<MemoEntity>
//
//    @Query("SELECT * FROM MemoEntity id=:id")
//    fun checkEntity() : Boolean

    @Insert
    fun insertMemo(entity: MemoEntity)

    @Update
    fun updateMemo(entity: MemoEntity)

    @Delete
    fun deleteMemo(entity: MemoEntity)
}