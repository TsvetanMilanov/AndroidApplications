package lab.chabingba.telerikacademyschedule;

import java.util.ArrayList;
import java.util.Calendar;

public final class Data {

    private static Calendar calendar;

    private static ArrayList<Event> listOfEvents = new ArrayList<>();

    private static ArrayList<Event> listOfOldEvents = new ArrayList<>();

    public static ArrayList<Event> GetListOfEvents() {
        return Data.listOfEvents;
    }

    public static void SetListValuesOfEvents(ArrayList<Event> events) {
        Data.listOfEvents = events;
    }

    public static ArrayList<Event> GetListOfOldEvents() {
        return Data.listOfOldEvents;
    }

    public static void SetListValuesOfOldEvents(ArrayList<Event> events) {
        Data.listOfOldEvents = events;

        if (Data.listOfOldEvents.size() > 0) {
            for (int i = 0; i < Data.listOfOldEvents.size(); i++) {
                Data.listOfOldEvents.get(i).SetIsFinished(true);
            }
        }
    }

    public static Calendar GetCalendar() {
        return Data.calendar;
    }

    public static void SetCalendar(Calendar value) {
        Data.calendar = value;
    }
}
