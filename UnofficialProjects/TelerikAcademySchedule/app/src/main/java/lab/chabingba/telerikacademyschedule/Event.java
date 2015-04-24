package lab.chabingba.telerikacademyschedule;

import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable, Comparable {

    public boolean isFinished;
    private int eventID;
    private String eventName;
    private Calendar eventDate;
    private String eventHour;
    private String eventDescription;

    public Event() {
        this.isFinished = false;
    }

    public Event(int eventID, String eventName, Calendar eventDate, String eventHour, String eventDescription) {
        this();
        this.SetEventName(eventName);
        this.SetEventDate(eventDate);
        this.SetEventHour(eventHour);
        this.SetEventDescription(eventDescription);
        this.SetEventID(eventID);
    }

    public String GetEventName() {
        return this.eventName;
    }

    public void SetEventName(String value) {
        this.eventName = value;
    }

    public String GetEventDate() {
        int year = this.eventDate.get(Calendar.YEAR);
        int month = this.eventDate.get(Calendar.MONTH) + 1;
        int day = this.eventDate.get(Calendar.DAY_OF_MONTH);

        String result = String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year);

        return result;
    }

    public Calendar GetEventDateAsCalendarDate() {
        return this.eventDate;
    }

    public void SetEventDate(Calendar value) {
        this.eventDate = value;
    }

    public String GetEventHour() {
        return this.eventHour;
    }

    public void SetEventHour(String value) {
        this.eventHour = value;
    }

    public String GetEventDescription() {
        return this.eventDescription;
    }

    public void SetEventDescription(String value) {
        this.eventDescription = value;
    }

    public int GetEventID() {
        return this.eventID;
    }

    public void SetEventID(int value) {
        this.eventID = value;
    }

    public String AsString() {
        StringBuilder result = new StringBuilder();

        result.append(this.GetEventID());
        result.append("\r\n");
        result.append(this.GetEventName());
        result.append("\r\n");
        result.append(this.GetEventDate());
        result.append("\r\n");
        result.append(this.GetEventHour());
        result.append("\r\n");
        result.append(this.GetEventDescription());
        result.append("\r\n");

        return result.toString().trim();
    }

    @Override
    public int compareTo(Object another) {
        Event anotherEvent = (Event) another;

        if (this.GetEventDateAsCalendarDate().compareTo(anotherEvent.GetEventDateAsCalendarDate()) == 0) {
            return 0;
        } else if (this.GetEventDateAsCalendarDate().compareTo(anotherEvent.GetEventDateAsCalendarDate()) > 0) {
            return 1;
        } else if (this.GetEventDateAsCalendarDate().compareTo(anotherEvent.GetEventDateAsCalendarDate()) < 0) {
            return -1;
        }

        return -1;
    }
}