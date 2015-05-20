package lab.chabingba.telerikacademyschedule.Helpers;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import lab.chabingba.telerikacademyschedule.Data;
import lab.chabingba.telerikacademyschedule.Event;

/**
 * Created by Tsvetan on 2015-05-18.
 */
public final class Engine {
    public static void StartEngine(Context context, File outputFile, ArrayList<Event> listOfEvents, File outputFileForOldEvents, ArrayList<Event> listOfOldEvents) {
        /* Update the output.txt if an event was edited
        (on app start Data.listOfEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, listOfEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        EventHelpers.SetInitialDate();

        //Do something only on first app run.
        EventHelpers.FirstAppRun(context, outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        FileHelpers.ReadEventsFromFile(listOfEvents, outputFile);

        FileHelpers.ReadEventsFromFile(listOfOldEvents, outputFileForOldEvents);

        Data.SetListValuesOfOldEvents(listOfOldEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        Collections.sort(listOfEvents);

        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        EventHelpers.StartAlarmReceiver(context);
    }

    public static void UpdateEngineFiles(Context context, File outputFile, ArrayList<Event> listOfEvents, File outputFileForOldEvents, ArrayList<Event> listOfOldEvents) {
        /* Update the output.txt if an event was edited
        (on app start Data.listOfEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, listOfEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        EventHelpers.SetInitialDate();

        //Do something only on first app run.
        EventHelpers.FirstAppRun(context, outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        FileHelpers.ReadEventsFromFile(listOfEvents, outputFile);

        FileHelpers.ReadEventsFromFile(listOfOldEvents, outputFileForOldEvents);

        Data.SetListValuesOfOldEvents(listOfOldEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        Collections.sort(listOfEvents);

        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);
    }
}
