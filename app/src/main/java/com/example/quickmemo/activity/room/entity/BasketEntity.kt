package com.example.quickmemo.activity.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BasketEntity(
     var last_date : String = "",
     var memo : String = "",
     @PrimaryKey(autoGenerate = true) var id : Int
) : Serializable {

}