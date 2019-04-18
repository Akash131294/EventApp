package com.eventapp.Model;

import com.eventapp.Utilities.HomeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Event {
    private String name, creatorName;
    private ArrayList<String> participants;
    private long timeStamp;

    public Event(String eventName, String eventCreatorName, ArrayList<String> eventParticipants, long timeStamp){
        this.name = eventName;
        this.creatorName = eventCreatorName;
        this.participants = eventParticipants;
        this.timeStamp = timeStamp;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getDate(){
        return HomeUtils.getDateFromTimestamp(timeStamp);
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getName() {
        return name;
    }
}
