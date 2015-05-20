package lab.chabingba.telerikacademyschedule.Helpers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import lab.chabingba.telerikacademyschedule.Event;

/**
 * Created by Tsvetan on 2015-04-23.
 */
public final class FileHelpers {

    public static void WriteEventsToFile(File outputFile, File templateFileDir, ArrayList<Event> listOfEvents) {
        if (listOfEvents.size() >= 0)
            try {
                if (outputFile.exists()) {
                    WriteOutput(outputFile, listOfEvents);
                } else {
                    CreateOutputFile(outputFile, templateFileDir);
                    WriteOutput(outputFile, listOfEvents);
                }
            } catch (IllegalAccessException e) {
                Log.e("I/O", e.getMessage());
            } catch (IOException e) {
                Log.e("I/O", e.getMessage());
            }
    }

    public static void WriteOutput(File outputFile, ArrayList<Event> listOfEvents) throws IOException {
        FileOutputStream outputStream;
        OutputStreamWriter outputStreamWriter;

        outputStream = new FileOutputStream(outputFile);
        outputStreamWriter = new OutputStreamWriter(outputStream);

        String allEventsAsString = MakeEventsString(listOfEvents);

        outputStreamWriter.append(allEventsAsString);
        outputStreamWriter.close();
    }

    public static void CreateOutputFile(File outputFile, File templateFileDir) throws IllegalAccessException {
        boolean storageIsAvailable = isExternalStorageWritable();

        if (!storageIsAvailable) {
            throw new IllegalAccessException("The external storage is not available.");
        }

        templateFileDir.mkdirs();

        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            Log.e("I/O", e.getMessage());
        }
    }

    public static void AddDefaultEvents(ArrayList<Event> listOfEvents, int daysToAdd) {
        Calendar date = Calendar.getInstance();

        int year;
        int month;
        int day;

        if (listOfEvents.size() <= 0) {
            date.setTime(new Date());
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH);
        } else {
            date = listOfEvents.get(listOfEvents.size() - 1).GetEventDateAsCalendarDate();
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH) + 1;
        }


        for (int i = 0; i < daysToAdd; i++) {
            Calendar dateToAdd = Calendar.getInstance();

            dateToAdd.set(year, month, day);
            listOfEvents.add(new Event(listOfEvents.size(), "Lecture", dateToAdd, "18:00", "Null"));
            day++;
        }
    }

    public static void ReadEventsFromFile(ArrayList<Event> listOfEvents, File outputFile) {

        StringBuilder allEventsAsStringBuilderAfterReading = new StringBuilder();

        if (listOfEvents.size() == 0) {
            try {
                FileInputStream inputStream = new FileInputStream(outputFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String currentLine = bufferedReader.readLine();

                while (currentLine != null) {
                    allEventsAsStringBuilderAfterReading.append(currentLine);
                    allEventsAsStringBuilderAfterReading.append("\r\n");

                    currentLine = bufferedReader.readLine();
                }

            } catch (FileNotFoundException e) {
                Log.e("READ", e.getMessage());
            } catch (IOException e) {
                Log.e("READ", e.getMessage());
            }
        }

        String allEventsAsStringAfterReading = allEventsAsStringBuilderAfterReading.toString();

        String[] allEventsArray = allEventsAsStringAfterReading.split("\r\n");

        if (allEventsArray.length <= 0) {
            throw new IllegalArgumentException("The event array is empty!");
        }

        for (int i = 0; i < allEventsArray.length; i++) {

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            int eventID = Integer.parseInt(allEventsArray[i]);
            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            String name = allEventsArray[i];
            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy");
            Date parsedDate = null;
            try {
                parsedDate = simpleDateFormat.parse(allEventsArray[i]);
            } catch (ParseException e) {
                Log.e("DATE", e.getMessage());
            }
            Calendar date = Calendar.getInstance();
            date.setTime(parsedDate);
            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            String hour = allEventsArray[i];
            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            String description = allEventsArray[i];
            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            boolean isFinished;

            if (allEventsArray[i].equals("true")) {
                isFinished = true;
            } else {
                isFinished = false;
            }

            i++;

            if (allEventsArray[i].length() <= 0 || i >= allEventsArray.length) {
                continue;
            }

            boolean hasNotification;

            if (allEventsArray[i].equals("true")) {
                hasNotification = true;
            } else {
                hasNotification = false;
            }

            Event newEventToAdd = new Event(eventID, name, date, hour, description);
            newEventToAdd.SetIsFinished(isFinished);
            newEventToAdd.SetHasNotification(hasNotification);

            if (newEventToAdd != null) {
                listOfEvents.add(newEventToAdd);
            }
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    public static void FirstInitList(File outputFile, ArrayList<Event> listOfEvents) {

            /*
            //Add days till the end of the month.

            Calendar today = Calendar.getInstance();
            today.setTime(new Date());

            int daysToAdd = GetDaysTillEndOfMonth(today);
            */

        int daysToAdd = 5;
        FileHelpers.AddDefaultEvents(listOfEvents, daysToAdd);

        FileHelpers.WriteEventsToFile(outputFile, Constants.FileDirectory, listOfEvents);
    }

    private static String MakeEventsString(ArrayList<Event> listOfEvents) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < listOfEvents.size(); i++) {
            String eventAsStringToAdd;
            eventAsStringToAdd = listOfEvents.get(i).AsString();

            result.append(eventAsStringToAdd);
            result.append("\r\n");
            result.append("\r\n");
        }
        return result.toString();
    }

    public static void DeleteAllFilesInDirectory(File directory) {
        File[] allFilesInDirectory = directory.listFiles();

        if (directory.exists()) {
            for (File tempFile : allFilesInDirectory) {
                tempFile.delete();
            }
        }
    }
}
