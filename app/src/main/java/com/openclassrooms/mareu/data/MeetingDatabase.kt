package com.openclassrooms.mareu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.mareu.model.Meeting
import kotlinx.coroutines.internal.synchronized

@Database(entities = [MeetingDB::class],version = 1,exportSchema = false)
abstract class MeetingDatabase: RoomDatabase() {

    abstract fun meetingDao(): MeetingDao

    companion object {
        @Volatile
        private var INSTANCE: MeetingDatabase? = null

        fun getDatabase(context: Context): MeetingDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            kotlin.synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MeetingDatabase::class.java,
                        "meeting_database"
                ).build()
                INSTANCE = instance
                return  instance
            }
        }
    }
}