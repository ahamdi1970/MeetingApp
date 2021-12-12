package com.openclassrooms.mareu.model

import java.util.*

/**
 * Model object representing a Meeting
 */
class Meeting(
        /**
         * Identifier
         */
        var id: Long,
        /**
         * Meeting name
         */
        var meetingName: String, date: String, hour: String,
        room: String, emails: String, color: Int) {

        /**
         * Meeting date
         */
        var date: String

        /**
         * Meeting hour
         */
        var hour: String

        /**
         * Meeting room
         */
        var room: String

        /**
         * Meeting emails
         */
        var emails: String

        /**
         * Color
         */
        var color: Int

        override fun equals(o: Any?): Boolean {
                if (this === o) return true
                if (o == null || javaClass != o.javaClass) return false
                val meeting = o as Meeting
                return id == meeting.id
        }

        override fun hashCode(): Int {
                return Objects.hash(id)
        }

        /**
         * Constructor
         *
         * @param id            long
         * @param meetingName   String
         * @param date          String
         * @param hour          String
         * @param room          String
         * @param emails        String
         * @param color      int
         */
        init {
                meetingName = meetingName
                this.date = date
                this.hour = hour
                this.room = room
                this.emails = emails
                this.color = color
        }
}