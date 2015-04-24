package lab.chabingba.telerikacademyschedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Tsvetan on 2015-04-23.
 */
public class EventNotificationService extends Service {
    private Event event;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("NOTIFSERVICE", "Service is created!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NOTIFSERVICE", "Service started");

        boolean hasEvent = CheckForEventToday();

        if (hasEvent) {
            Log.i("EVENT", "Has notification");

            Intent intentForEdit = new Intent(EventNotificationService.this, SingleEventViewActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("BundleEvent", this.event);
            bundle.putSerializable("List", Data.listOfEvents);

            intentForEdit.putExtras(bundle);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentForEdit, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Notification notification = new Notification(R.drawable.icon, "Telerik Academy Event!", System.currentTimeMillis());

            notification.setLatestEventInfo(this, "Telerik Academy Event!!!", this.event.GetEventName() + " " + this.event.GetEventHour(), pendingIntent);

            /*
            //Uncomment for auto cancel the notification.
            notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;
            */

            notificationManager.notify(1, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean CheckForEventToday() {

        Calendar currentDate = Calendar.getInstance();

        Calendar dateOfEvent;

        for (int i = 0; i < Data.listOfEvents.size(); i++) {
            dateOfEvent = Data.listOfEvents.get(i).GetEventDateAsCalendarDate();

            if (dateOfEvent.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                    && dateOfEvent.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)
                    && dateOfEvent.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                this.event = Data.listOfEvents.get(i);
                Log.i("EVENTCHECK", "Found event for today.");
                return true;
            }
        }

        Log.i("EVENTCHECK", "No event for today.");
        return false;
    }
}
