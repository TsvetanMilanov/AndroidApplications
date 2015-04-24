package lab.chabingba.telerikacademyschedule;

import android.app.ListActivity;
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
import java.util.ArrayList;
import java.util.Collections;

import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.ListActivityHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.UpdateHelpers;

public class MainActivity extends ListActivity {
    public static MainActivity contextOfMainActivity;
    public ArrayList<Event> listOfEvents = Data.listOfEvents;
    private File templateFileDir = new File(Environment.getExternalStorageDirectory() + "/TelerikScheduleAPP/");
    private File outputFile = new File(templateFileDir, "output.txt");

    public static Context GetContext() {
        return contextOfMainActivity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(85, 255, 40));

        //Set the value of the Content variable to use from another classes.
        contextOfMainActivity = this;

        /* Update the output.txt if an event was edited
        (on app start Data.listOfEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, templateFileDir, listOfEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, templateFileDir, listOfEvents);

        //Do something only on first app run.
        ListActivityHelpers.FirstAppRun(this, outputFile, templateFileDir, listOfEvents);

        FileHelpers.ReadEventsFromFile(listOfEvents, outputFile);

        Data.SetListValues(listOfEvents);

        Collections.sort(listOfEvents);

        UpdateHelpers.UpdateIndexes(outputFile, templateFileDir, listOfEvents);

        /* Create string array with all event names and dates as string for list items. */
        String[] eventsThumbnails = ListActivityHelpers.CreateEventThumbnails(listOfEvents);

        setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, eventsThumbnails));
        registerForContextMenu(this.getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Event eventToOpen = listOfEvents.get(position);


        Intent intent1 = new Intent(MainActivity.this, SingleEventViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("BundleEvent", eventToOpen);
        bundle.putSerializable("List", listOfEvents);

        intent1.putExtras(bundle);
        startActivity(intent1);
        finish();
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

                    Intent intentForAdd = new Intent(MainActivity.this, SingleEventEditActivity.class);

                    Bundle bundle = new Bundle();

                    Event eventToPass = listOfEvents.get(listOfEvents.size() - 1);

                    bundle.putSerializable("Event", eventToPass);

                    intentForAdd.putExtras(bundle);

                    startActivity(intentForAdd);
                    finish();
                break;
            case R.id.addTenEvents:

                FileHelpers.AddDefaultEvents(listOfEvents, 10);

                startActivity(intent);
                finish();
                break;
            case R.id.oldEvents:

                Intent intentForOldEventsActivity = new Intent(MainActivity.this, OldEventsActivity.class);

                startActivity(intentForOldEventsActivity);
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

                    Intent intent1 = new Intent(MainActivity.this, SingleEventEditActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Event", eventToOpen);
                    bundle.putSerializable("List", listOfEvents);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    finish();
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
}
