package lab.chabingba.telerikacademyschedule.Helpers;

import android.os.Environment;

import java.io.File;

/**
 * Created by Tsvetan on 2015-04-25.
 */
public final class Constants {
    public static final File FileDirectory = new File(Environment.getExternalStorageDirectory() + "/TelerikScheduleAPP/");
    public static final File FileDirectoryForBackup = new File(Environment.getExternalStorageDirectory() + "/TelerikScheduleAPP/Backup");

    public enum EventType{
        Lecture,
        Workshop,
        Non_Technical_Lecture,
        Seminar,
        Exam;
    }
}
