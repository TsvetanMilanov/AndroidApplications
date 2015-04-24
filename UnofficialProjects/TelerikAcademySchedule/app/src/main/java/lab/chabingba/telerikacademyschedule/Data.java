package lab.chabingba.telerikacademyschedule;

import java.util.ArrayList;
import java.util.Calendar;

public final class Data {

    public static Calendar calendar;

    public static ArrayList<Event> listOfEvents = new ArrayList<>();

    public static ArrayList<Event> listOfOldEvents = new ArrayList<>();

    public static void SetListValues(ArrayList<Event> events) {
        Data.listOfEvents = events;
    }
}
