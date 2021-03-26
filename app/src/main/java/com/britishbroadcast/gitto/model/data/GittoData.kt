package com.britishbroadcast.gitto.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GittoData(
        @PrimaryKey(autoGenerate = true) val itemId: Int,
        @ColumnInfo(name = "userName") val userName: String,
        @ColumnInfo(name = "info") val userData: String)
{
        constructor(userName: String, userData: String): this(0, userName, userData)
}