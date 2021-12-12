package com.openclassrooms.mareu.data

import androidx.lifecycle.LiveData

class MeetingRepository(private val meetingDao: MeetingDao) {

    val readAllData: LiveData<List<MeetingDB>> = meetingDao.readAllData()

    suspend fun addMeeting(meeting: MeetingDB){
        meetingDao.addMareu(meeting)
    }

}