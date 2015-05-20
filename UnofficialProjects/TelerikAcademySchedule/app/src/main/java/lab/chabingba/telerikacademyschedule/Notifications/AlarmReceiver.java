package lab.chabingba.telerikacademyschedule.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Helpers.Engine;
import lab.chabingba.telerikacademyschedule.Helpers.EventHelpers;

/**
 * Created by Tsvetan on 2015-05-14.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RECEIVER", "RECEIVED");
        Engine.UpdateEngineFiles(context, Data.GetOutputFile(), Data.GetListOfEvents(), Data.GetOutputFileForOldEvents(), Data.GetListOfOldEvents());
        EventHelpers.AlarmForPendingEvent(context);
    }
}
