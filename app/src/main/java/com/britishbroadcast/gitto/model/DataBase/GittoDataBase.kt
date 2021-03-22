package com.britishbroadcast.gitto.model.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.britishbroadcast.gitto.model.data.GittoData

@Database(entities = [GittoData::class], version = 1, exportSchema = false)
abstract  class GittoDataBase: RoomDatabase() {
    abstract fun gittoDAO(): GittoDAO
}