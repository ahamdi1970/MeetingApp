package com.openclassrooms.mareu.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.mareu.model.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeetingViewModel(application: Application):AndroidViewModel(application) {
    private val readAllData: LiveData<List<MeetingDB>>
    private val repository: MeetingRepository

    init {
        val meetingDao = MeetingDatabase.getDatabase(application).meetingDao()
        repository = MeetingRepository(meetingDao)
        readAllData = repository.readAllData
    }

    fun addMeeting(meeting: MeetingDB){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMeeting(meeting) }
    }
}