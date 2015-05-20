package lab.chabingba.telerikacademyschedule.Helpers;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import lab.chabingba.telerikacademyschedule.Event;

public final class UpdateHelpers {

    public static void UpdateOutputFile(File outputFile, ArrayList<Event> listOfEvents) {
        if (listOfEvents.size() > 0) {
            outputFile.delete();

            try {
                FileHelpers.CreateOutputFile(outputFile, Constants.FileDirectory);
            } catch (IllegalAccessException e) {
                Log.e("FILE", e.getMessage());
            }

            FileHelpers.WriteEventsToFile(outputFile, Constants.FileDirectory, listOfEvents);
        }
    }

    public static void UpdateIndexes(File outputFile, ArrayList<Event> listOfEvents) {
        for (int i = 0; i < listOfEvents.size(); i++) {
            listOfEvents.get(i).SetEventID(i);
        }

        if (listOfEvents.size() > 0) {
            outputFile.delete();

            try {
                FileHelpers.CreateOutputFile(outputFile, Constants.FileDirectory);
            } catch (IllegalAccessException e) {
                Log.e("FILE", e.getMessage());
            }

            FileHelpers.WriteEventsToFile(outputFile, Constants.FileDirectory, listOfEvents);
        }
    }
}
