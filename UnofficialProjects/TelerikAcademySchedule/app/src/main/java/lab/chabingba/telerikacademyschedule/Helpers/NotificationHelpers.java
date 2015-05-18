package lab.chabingba.telerikacademyschedule.Helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;
import lab.chabingba.telerikacademyschedule.MainActivity;
import lab.chabingba.telerikacademyschedule.R;
import lab.chabingba.telerikacademyschedule.SingleEventViewActivity;

/**
 * Created by Tsvetan on 2015-05-14.
 */
public class NotificationHelpers {
    public static Event CheckForEventToday() {
        Event event = null;

        Calendar currentDate = Calendar.getInstance();

        Calendar dateOfEvent;

        for (int i = 0; i < Data.GetListOfEvents().size(); i++) {
            dateOfEvent = Data.GetListOfEvents().get(i).GetEventDateAsCalendarDate();

            if (dateOfEvent.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                    && dateOfEvent.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)
                    && dateOfEvent.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                event = Data.GetListOfEvents().get(i);
                Log.i("EVENTCHECK", "Found event for today.");
                return event;
            }
        }

        Log.i("EVENTCHECK", "No event for today.");
        return null;
    }

    public static void CreateNotification(Event event) {
        Log.i("EVENT", "Has notification");

        Intent intentForEdit = new Intent(MainActivity.GetContext(), SingleEventViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("BundleEvent", event);
        bundle.putSerializable("List", Data.GetListOfEvents());

        intentForEdit.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.GetContext(), 0, intentForEdit, 0);

        Calendar date = Calendar.getInstance();

        date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(event.GetEventHour().split(":")[0]));
        date.set(Calendar.MINUTE, Integer.parseInt(event.GetEventHour().split(":")[1].split(" ")[0]));

        long dateAsLong = date.getTimeInMillis();

        NotificationManager notificationManager = (NotificationManager) MainActivity.GetContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.icon, "Telerik Academy Event!", dateAsLong);

        notification.setLatestEventInfo(MainActivity.GetContext(), "Telerik Academy Event!!!", event.GetEventName(), pendingIntent);

        notification.icon = R.drawable.icon;

        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            /*
            //Uncomment for auto cancel the notification.
            notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;
            */

        notificationManager.notify(1, notification);
    }
}
