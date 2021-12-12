package com.openclassrooms.mareu.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_table")
data class MeetingDB(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val meetingName: String,
        val meetingDate: String,
        val meetingHour: String,
        val meetingRoom: String,
        val meetingEmails: String,
        val meetingColor: Int
)