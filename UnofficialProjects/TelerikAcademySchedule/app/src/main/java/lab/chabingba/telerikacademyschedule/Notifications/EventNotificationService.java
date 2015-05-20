package lab.chabingba.telerikacademyschedule.Notifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;
import lab.chabingba.telerikacademyschedule.Helpers.NotificationHelpers;

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

        this.event = NotificationHelpers.CheckForEventToday();

        if (this.event != null && event.GetHasNotification() == false) {
            NotificationHelpers.CreateNotification(this, event);
            Data.GetListOfEvents().get(event.GetEventID()).SetHasNotification(true);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
