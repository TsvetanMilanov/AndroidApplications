package lab.chabingba.telerikacademyschedule.Helpers;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;

public final class UpdateHelpers {

    public static void UpdateOutputFile(File outputFile, File templateFileDir, ArrayList<Event> listOfEvents) {
        if (Data.listOfEvents.size() > 0) {
            outputFile.delete();

            try {
                FileHelpers.CreateOutputFile(outputFile, templateFileDir);
            } catch (IllegalAccessException e) {
                Log.e("FILE", e.getMessage());
            }

            FileHelpers.WriteEventsToFile(outputFile, templateFileDir, listOfEvents);
        }
    }

    public static void UpdateIndexes(File outputFile, File templateFileDir, ArrayList<Event> listOfEvents) {
        for (int i = 0; i < listOfEvents.size(); i++) {
            listOfEvents.get(i).SetEventID(i);
        }

        if (Data.listOfEvents.size() > 0) {
            outputFile.delete();

            try {
                FileHelpers.CreateOutputFile(outputFile, templateFileDir);
            } catch (IllegalAccessException e) {
                Log.e("FILE", e.getMessage());
            }

            Data.SetListValues(listOfEvents);
            FileHelpers.WriteEventsToFile(outputFile, templateFileDir, listOfEvents);
        }
    }
}
