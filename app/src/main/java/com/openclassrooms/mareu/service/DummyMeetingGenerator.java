package com.openclassrooms.mareu.service;


import com.openclassrooms.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingGenerator {


    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(1, "Réunion A", "20-10-2021", "08h00", "Mozart", "mouloud@tata.com", -155 ),
            new Meeting(2, "Réunion B", "23-10-2021", "09h00", "Mozart", "farouk@tata.com", -89 ),
            new Meeting(3, "Réunion C", "25-10-2021", "10h00", "Beethoven", "jean@tata.com", 25 - 255 )
    );

    static List<Meeting> readDBMeetings() {


        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
