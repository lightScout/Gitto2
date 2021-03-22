package com.britishbroadcast.gitto.model.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.britishbroadcast.gitto.model.data.GittoData
@Dao
interface GittoDAO {

    @Insert
    fun insertGittoItem(vararg gittoData: GittoData)

    @Query("SELECT * FROM gittodata")
    fun getAllItems(): List<GittoData>


}