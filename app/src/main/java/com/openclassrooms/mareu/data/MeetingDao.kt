package com.openclassrooms.mareu.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.mareu.model.Meeting

@Dao
interface MeetingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMareu(meeting : MeetingDB)

    @Query("SELECT * FROM meeting_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<MeetingDB>>

}