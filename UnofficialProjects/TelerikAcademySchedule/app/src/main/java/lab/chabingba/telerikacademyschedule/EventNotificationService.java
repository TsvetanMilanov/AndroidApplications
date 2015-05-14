package lab.chabingba.telerikacademyschedule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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

        if (this.event != null) {
            NotificationHelpers.CreateNotification(event);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
