package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import lab.chabingba.telerikacademyschedule.Helpers.Constants;
import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;

/**
 * Created by Tsvetan on 2015-05-20.
 */
public class OptionsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);

        ListView listView = (ListView) findViewById(R.id.lvOptions);

        String[] optionsArray = {"Backup events", "Load events from backup"};

        ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, optionsArray);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                FileHelpers.DeleteAllFilesInDirectory(Constants.FileDirectoryForBackup);

                                File outputFile = new File(Constants.FileDirectoryForBackup, "output.txt");

                                FileHelpers.WriteEventsToFile(outputFile, Constants.FileDirectoryForBackup, Data.GetListOfEvents());

                                File outputFileForOldEvents = new File(Constants.FileDirectoryForBackup, "OldEvents.txt");
                                FileHelpers.WriteEventsToFile(outputFileForOldEvents, Constants.FileDirectoryForBackup, Data.GetListOfOldEvents());

                                if (Data.GetListOfEvents().size() > 0 || Data.GetListOfOldEvents().size() > 0) {
                                    if (Data.GetListOfEvents().size() > 0 && outputFile.getTotalSpace() > 0) {
                                        Log.i("BACKUP", "Successful backup of events.");
                                        if (Data.GetListOfOldEvents().size() > 0 && outputFileForOldEvents.getTotalSpace() > 0) {
                                            MakeLongToast("Backup of events completed successfully");
                                            Log.i("BACKUP", "Successful backup of old events.");
                                        } else if (Data.GetListOfOldEvents().size() <= 0) {
                                            MakeLongToast("Backup of events completed successfully");
                                        } else if (Data.GetListOfOldEvents().size() > 0) {
                                            Log.i("BACKUP", "Error in backup of old events");
                                        }
                                    } else if (Data.GetListOfEvents().size() <= 0) {
                                        if (Data.GetListOfOldEvents().size() > 0 && outputFileForOldEvents.getTotalSpace() > 0) {
                                            MakeLongToast("Backup of events completed successfully");
                                            Log.i("BACKUP", "Successful backup of old events.");
                                        } else if (Data.GetListOfOldEvents().size() > 0) {
                                            Log.i("BACKUP", "Error in backup of old events");
                                        }
                                    } else if (Data.GetListOfEvents().size() > 0) {
                                        MakeLongToast("Unsuccessful backup of events");
                                        Log.i("BACKUP", "Error in backup of events");
                                    }
                                } else {
                                    MakeLongToast("There are no events to backup");
                                }
                                break;
                            case 1:

                                break;
                            default:
                                MakeLongToast("Default case...");
                                break;
                        }
                    }
                }
        );
    }

    private void MakeLongToast(String message) {
        Toast.makeText(OptionsActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
