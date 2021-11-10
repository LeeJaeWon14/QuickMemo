package com.example.quickmemo.activity.room.dao

import androidx.room.*
import com.example.quickmemo.activity.room.entity.BasketEntity
import com.example.quickmemo.activity.room.entity.MemoEntity

@Dao
interface BasketDAO {
    @Query("SELECT * FROM BasketEntity")
    fun getBasketList() : List<MemoEntity>

    @Insert
    fun insertBasket(entity: BasketEntity)

    @Update
    fun updateBasket(entity: BasketEntity)

    @Delete
    fun deleteBasket(entity: BasketEntity)
}