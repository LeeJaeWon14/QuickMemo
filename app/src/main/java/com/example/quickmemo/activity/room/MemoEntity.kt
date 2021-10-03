package com.example.quickmemo.activity.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class MemoEntity(
     var last_date : String = "",
     var memo : String = "",
     @PrimaryKey(autoGenerate = true) var id : Int = 0
) : Serializable {

}