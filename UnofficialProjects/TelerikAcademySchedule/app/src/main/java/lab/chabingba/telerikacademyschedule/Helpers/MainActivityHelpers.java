package lab.chabingba.telerikacademyschedule.Helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;
import lab.chabingba.telerikacademyschedule.EventNotificationService;
import lab.chabingba.telerikacademyschedule.MainActivity;

/**
 * Created by Tsvetan on 2015-04-24.
 */
public final class MainActivityHelpers {

    public static void FirstAppRun(Context context, File outputFile, FileOutputStream outputStream, OutputStreamWriter outputStreamWriter, File templateFileDir, ArrayList<Event> listOfEvents) {
        boolean isFirstAppRun;
        SharedPreferences sharedPreferences;

        sharedPreferences = context.getSharedPreferences("PREFS_NAME", 0);

        isFirstAppRun = sharedPreferences.getBoolean("FIRST_RUN", false);

        if (!isFirstAppRun) {

        /* Create the list of events on the first app run. */
            FileHelpers.FirstInitList(MainActivity.GetContext(), outputFile, outputStream, outputStreamWriter, templateFileDir, listOfEvents);

            MainActivityHelpers.SetInitialDate();

            MainActivityHelpers.AlarmForPendingEvent();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        }
    }

    private static void AlarmForPendingEvent() {

        Intent intentForNotification = new Intent(MainActivity.GetContext(), EventNotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.GetContext(), 0, intentForNotification, 0);

        AlarmManager alarmManager = (AlarmManager) MainActivity.GetContext().getSystemService(Context.ALARM_SERVICE);

        //Set the notification time.
        Data.calendar.set(Calendar.HOUR, 10);
        Data.calendar.set(Calendar.MINUTE, 0);
        Data.calendar.set(Calendar.SECOND, 0);
        Data.calendar.set(Calendar.AM_PM, Calendar.AM);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Data.calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
    }

    private static void SetInitialDate() {
        Data.calendar = Calendar.getInstance();
        Data.calendar.add(Calendar.SECOND, 3);
    }

    private int GetDaysTillEndOfMonth(Calendar fromDate) {
        Calendar date = fromDate;

        int currentDays = date.get(Calendar.DAY_OF_MONTH);
        int currentMonth = date.get(Calendar.MONTH);
        int currentYear = date.get(Calendar.YEAR);

        int maxDays = date.getActualMaximum(Calendar.DAY_OF_MONTH);

        int result;

        result = maxDays - currentDays + 1;

        return result;
    }
}
