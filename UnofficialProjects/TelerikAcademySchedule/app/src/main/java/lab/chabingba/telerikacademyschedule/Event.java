package lab.chabingba.telerikacademyschedule;

import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable, Comparable {

    private boolean isFinished;
    private int eventID;
    private String eventType;
    private Calendar eventDate;
    private String eventHour;
    private String eventDescription;
    private boolean hasNotification;

    public Event() {
        this.isFinished = false;
        this.hasNotification = false;
    }

    public Event(int eventID, String eventType, Calendar eventDate, String eventHour, String eventDescription) {
        this();
        this.SetEventType(eventType);
        this.SetEventDate(eventDate);
        this.SetEventHour(eventHour);
        this.SetEventDescription(eventDescription);
        this.SetEventID(eventID);
    }

    public String GetEventType() {
        return this.eventType;
    }

    public void SetEventType(String value) {
        this.eventType = value;
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
        result.append(this.GetEventType());
        result.append("\r\n");
        result.append(this.GetEventDate());
        result.append("\r\n");
        result.append(this.GetEventHour());
        result.append("\r\n");
        result.append(this.GetEventDescription());
        result.append("\r\n");
        if (this.GetIsFinished() == true) {
            result.append("true");
        } else {
            result.append("false");
        }
        result.append("\r\n");
        if (this.GetHasNotification() == true) {
            result.append("true");
        } else {
            result.append("false");
        }
        result.append("\r\n");

        return result.toString().trim();
    }

    public boolean GetIsFinished() {
        return this.isFinished;
    }

    public void SetIsFinished(boolean value) {
        this.isFinished = value;
    }

    public boolean GetHasNotification() {
        return this.hasNotification;
    }

    public void SetHasNotification(boolean value) {
        this.hasNotification = value;
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

    public boolean Compare(Event secondEvent) {
        if (!this.GetEventType().equals(secondEvent.GetEventType())) {
            return false;
        }

        if (!this.GetEventDate().equals(secondEvent.GetEventDate())) {
            return false;
        }

        if (!this.GetEventHour().equals(secondEvent.GetEventHour())) {
            return false;
        }

        if (!this.GetEventDescription().equals(secondEvent.GetEventDescription())) {
            return false;
        }

        if (this.GetIsFinished() != secondEvent.GetIsFinished()) {
            return false;
        }

        if (this.GetHasNotification() != secondEvent.GetHasNotification()) {
            return false;
        }

        return true;
    }
}
