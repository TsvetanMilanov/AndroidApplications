package lab.chabingba.telerikacademyschedule;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.MainActivityHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.UpdateHelpers;

public class MainActivity extends ListActivity {
    public ArrayList<Event> listOfEvents = Data.listOfEvents;
    public static MainActivity contextOfMainActivity;

    private File templateFileDir = new File(Environment.getExternalStorageDirectory() + "/TelerikScheduleAPP/");
    private File outputFile = new File(templateFileDir, "output.txt");
    private FileOutputStream outputStream;
    private OutputStreamWriter outputStreamWriter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(85, 255, 40));

        //Set the value of the Content variable to use from another classes.
        contextOfMainActivity = this;

        /* Update the output.txt if an event was edited
        (on app start Data.listOfEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, outputStream, outputStreamWriter, templateFileDir, listOfEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, outputStream, outputStreamWriter, templateFileDir, listOfEvents);

        //Do something only on first app run.
        MainActivityHelpers.FirstAppRun(this, outputFile, outputStream, outputStreamWriter, templateFileDir, listOfEvents);

        FileHelpers.ReadEventsFromFile(listOfEvents, outputFile);

        Data.SetListValues(listOfEvents);

        Collections.sort(listOfEvents);

        UpdateHelpers.UpdateIndexes(outputFile, outputStream, outputStreamWriter, templateFileDir, listOfEvents);

        /* Create string array with all event names and dates as string for list items. */
        String[] eventsThumbnails = CreateEventThumbnails(listOfEvents);

        setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, eventsThumbnails));
        registerForContextMenu(this.getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Event eventToOpen = listOfEvents.get(position);

        Class class1;
        try {
            class1 = Class.forName("lab.chabingba.telerikacademyschedule.SingleEventView");
            Intent intent1 = new Intent(MainActivity.this, class1);

            Bundle bundle = new Bundle();
            bundle.putSerializable("BundleEvent", eventToOpen);
            bundle.putSerializable("List", listOfEvents);

            intent1.putExtras(bundle);
            startActivity(intent1);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater;
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);

        switch (item.getItemId()) {
            case R.id.addEvent:

                FileHelpers.AddDefaultEvents(listOfEvents, 1);
                Data.SetListValues(listOfEvents);
                Class nextClass;

                try {
                    nextClass = Class.forName("lab.chabingba.telerikacademyschedule.SingleEventEdit");

                    Intent intentForAdd = new Intent(MainActivity.this, nextClass);

                    Bundle bundle = new Bundle();

                    Event eventToPass = listOfEvents.get(listOfEvents.size() - 1);

                    bundle.putSerializable("Event", eventToPass);

                    intentForAdd.putExtras(bundle);

                    startActivity(intentForAdd);
                    finish();
                } catch (ClassNotFoundException e) {
                    Log.e("CLASS", e.getMessage());
                }
                break;
            case R.id.addTenEvents:

                FileHelpers.AddDefaultEvents(listOfEvents, 10);

                startActivity(intent);
                finish();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.long_click_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = info.position;

        switch (item.getItemId()) {
            case R.id.editEvent:
                Event eventToOpen = listOfEvents.get(id);

                Class class1;
                try {
                    class1 = Class.forName("lab.chabingba.telerikacademyschedule.SingleEventEdit");
                    Intent intent1 = new Intent(MainActivity.this, class1);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Event", eventToOpen);
                    bundle.putSerializable("List", listOfEvents);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.removeEvent:
                for (int i = 0; i < Data.listOfEvents.size(); i++) {
                    Event currentEvent = Data.listOfEvents.get(i);

                    if (currentEvent.GetEventID() == listOfEvents.get(id).GetEventID()) {
                        Data.listOfEvents.remove(i);
                        break;
                    }
                }
                Intent setIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(setIntent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private String[] CreateEventThumbnails(ArrayList<Event> listOfEvents) {
        String[] eventsNamesAndDates = new String[listOfEvents.size()];

        for (int i = 0; i < listOfEvents.size(); i++) {
            Event currentEvent = listOfEvents.get(i);

            String currentDay = FindDayOfWeek(currentEvent.GetEventDateAsCalendarDate().get(Calendar.DAY_OF_WEEK));

            eventsNamesAndDates[i] = currentEvent.GetEventName() + "\r\n" + currentEvent.GetEventDate() + "\r\n" + currentDay + "\r\n" + currentEvent.GetEventHour() + " h";
        }

        return eventsNamesAndDates;
    }

    private String FindDayOfWeek(int intDay) {
        String result = null;

        switch (intDay) {
            case 1:
                result = "Sunday";
                return result;
            case 2:
                result = "Monday";
                return result;
            case 3:
                result = "Tuesday";
                return result;
            case 4:
                result = "Wednesday";
                return result;
            case 5:
                result = "Thursday";
                return result;
            case 6:
                result = "Friday";
                return result;
            case 7:
                result = "Saturday";
                return result;
        }

        return result;
    }

    public static Context GetContext(){
        return contextOfMainActivity;
    }
}
