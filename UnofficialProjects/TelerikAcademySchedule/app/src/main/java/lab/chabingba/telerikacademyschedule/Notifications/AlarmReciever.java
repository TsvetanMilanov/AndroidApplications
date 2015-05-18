package lab.chabingba.telerikacademyschedule.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import lab.chabingba.telerikacademyschedule.Helpers.ListActivityHelpers;

/**
 * Created by Tsvetan on 2015-05-14.
 */
public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RECEIVER", "RECEIVED");
        ListActivityHelpers.AlarmForPendingEvent();
    }
}
