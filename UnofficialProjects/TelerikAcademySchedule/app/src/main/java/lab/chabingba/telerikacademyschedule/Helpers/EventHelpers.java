package lab.chabingba.telerikacademyschedule.Helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;
import lab.chabingba.telerikacademyschedule.CurrentEventsActivity;
import lab.chabingba.telerikacademyschedule.Notifications.AlarmReceiver;
import lab.chabingba.telerikacademyschedule.Notifications.EventNotificationService;

/**
 * Created by Tsvetan on 2015-04-24.
 */
public final class EventHelpers {

    public static void FirstAppRun(Context context, File outputFile, ArrayList<Event> listOfEvents) {
        boolean isFirstAppRun;
        SharedPreferences sharedPreferences;

        sharedPreferences = context.getSharedPreferences("PREFS_NAME", 0);

        isFirstAppRun = sharedPreferences.getBoolean("FIRST_RUN", false);

        if (!isFirstAppRun) {

        /* Create the list of events on the first app run. */
            FileHelpers.FirstInitList(outputFile, listOfEvents);

            EventHelpers.AlarmForPendingEvent(context);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        }
    }

    public static String FindDayOfWeek(int intDay) {
        String result = null;

        switch (intDay) {
            case 1:
                result = "Sunday";
                return result;
            case 2:
                result = "Monday";
                return result;
            case 3:
                result = "Tuesday";
                return result;
            case 4:
                result = "Wednesday";
                return result;
            case 5:
                result = "Thursday";
                return result;
            case 6:
                result = "Friday";
                return result;
            case 7:
                result = "Saturday";
                return result;
            default:
                result = "No day :D";
                break;
        }

        return result;
    }

    public static String[] CreateEventThumbnails(ArrayList<Event> listOfEvents) {
        String[] eventsNamesAndDates = new String[listOfEvents.size()];

        for (int i = 0; i < listOfEvents.size(); i++) {
            Event currentEvent = listOfEvents.get(i);

            String currentDay = EventHelpers.FindDayOfWeek(currentEvent.GetEventDateAsCalendarDate().get(Calendar.DAY_OF_WEEK));

            eventsNamesAndDates[i] = currentEvent.GetEventName() + "\r\n" + currentEvent.GetEventDate() + "\r\n" + currentDay + "\r\n" + currentEvent.GetEventHour() + " h";
        }

        return eventsNamesAndDates;
    }

    public static void AlarmForPendingEvent(Context context) {
        Log.i("ALARM", "AlarmForPendingIntentStarted");

        Intent intentForNotification = new Intent(context, EventNotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentForNotification, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Set the notification time.
        Data.GetCalendar().set(Calendar.HOUR, 10);
        Data.GetCalendar().set(Calendar.MINUTE, 0);
        Data.GetCalendar().set(Calendar.SECOND, 0);
        Data.GetCalendar().set(Calendar.AM_PM, Calendar.AM);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Data.GetCalendar().getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
    }

    public static void SetInitialDate() {
        Data.SetCalendar(Calendar.getInstance());
        Data.GetCalendar().add(Calendar.SECOND, 3);
    }

    public static int GetDaysTillEndOfMonth(Calendar fromDate) {
        Calendar date = fromDate;

        int currentDays = date.get(Calendar.DAY_OF_MONTH);
        int currentMonth = date.get(Calendar.MONTH);
        int currentYear = date.get(Calendar.YEAR);

        int maxDays = date.getActualMaximum(Calendar.DAY_OF_MONTH);

        int result;

        result = maxDays - currentDays + 1;

        return result;
    }

    public static void StartAlarmReceiver(Context context) {
        Intent intentForNotification = new Intent(context, AlarmReceiver.class);
        context.sendBroadcast(intentForNotification);
    }

    private static void MoveEventToOldEvents(ArrayList<Event> listOfEvents, Event currentEvent) {
        currentEvent.SetHasNotification(true);
        Data.GetListOfOldEvents().add(currentEvent);
        listOfEvents.remove(currentEvent);
        Data.SetListValuesOfEvents(listOfEvents);
    }

    public static void RemoveOldEvents(ArrayList<Event> listOfEvents) {
        Calendar currentDate = Calendar.getInstance();
        Calendar eventDate;

        while (true) {
            Event currentEvent = listOfEvents.get(0);

            eventDate = currentEvent.GetEventDateAsCalendarDate();

            if (eventDate.get(Calendar.YEAR) > currentDate.get(Calendar.YEAR)) {
                break;
            } else if (eventDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
                if (eventDate.get(Calendar.MONTH) > currentDate.get(Calendar.MONTH)) {
                    break;
                } else if (eventDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
                    if (eventDate.get(Calendar.DAY_OF_MONTH) > currentDate.get(Calendar.DAY_OF_MONTH)) {
                        break;
                    } else if (eventDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                        break;
                    } else {
                        EventHelpers.MoveEventToOldEvents(listOfEvents, currentEvent);
                    }
                } else {
                    EventHelpers.MoveEventToOldEvents(listOfEvents, currentEvent);
                }
            } else {
                EventHelpers.MoveEventToOldEvents(listOfEvents, currentEvent);
            }
        }
    }
}
