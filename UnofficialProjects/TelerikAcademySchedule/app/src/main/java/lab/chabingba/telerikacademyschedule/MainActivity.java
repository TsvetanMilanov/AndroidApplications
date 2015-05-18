package lab.chabingba.telerikacademyschedule;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import lab.chabingba.telerikacademyschedule.Helpers.Constants;
import lab.chabingba.telerikacademyschedule.Helpers.EventHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.UpdateHelpers;

public class MainActivity extends ListActivity {
    private static MainActivity contextOfMainActivity;
    private ArrayList<Event> listOfEvents = Data.GetListOfEvents();
    private ArrayList<Event> listOfOldEvents = Data.GetListOfOldEvents();

    private File outputFile = new File(Constants.TemplateFileDir, "output.txt");
    private File outputFileForOldEvents = new File(Constants.TemplateFileDir, "OldEvents.txt");

    public static Context GetContext() {
        return contextOfMainActivity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.WHITE);

        //Set the value of the Content variable to use from another classes.
        contextOfMainActivity = this;

        EventHelpers.StartAlarmReceiver(this);

        /* Update the output.txt if an event was edited
        (on app start Data.listOfEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, listOfEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        EventHelpers.SetInitialDate();

        //Do something only on first app run.
        EventHelpers.FirstAppRun(this, outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        FileHelpers.ReadEventsFromFile(listOfEvents, outputFile);

        FileHelpers.ReadEventsFromFile(listOfOldEvents, outputFileForOldEvents);

        Data.SetListValuesOfOldEvents(listOfOldEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        Collections.sort(listOfEvents);

        UpdateHelpers.UpdateIndexes(outputFile, listOfEvents);

        Data.SetListValuesOfEvents(listOfEvents);

        /* Create string array with all event names and dates as string for list items. */
        String[] eventsThumbnails = EventHelpers.CreateEventThumbnails(listOfEvents);

        setListAdapter(new CustomAdapter(this, eventsThumbnails));
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
                Data.SetListValuesOfEvents(listOfEvents);

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
            case R.id.removeOldEvents:
                EventHelpers.RemoveOldEvents(listOfEvents);
                Intent refreshMainIntent = new Intent(MainActivity.this, MainActivity.class);

                startActivity(refreshMainIntent);
                finish();
                break;

            case R.id.forceNotifications:
                for (int i = 0; i < this.listOfEvents.size(); i++) {
                    if (listOfEvents.get(i).GetHasNotification()) {
                        listOfEvents.get(i).SetHasNotification(false);
                    }
                }
                EventHelpers.StartAlarmReceiver(this);
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
                for (int i = 0; i < Data.GetListOfEvents().size(); i++) {
                    Event currentEvent = Data.GetListOfEvents().get(i);

                    if (currentEvent.GetEventID() == listOfEvents.get(id).GetEventID()) {
                        Data.GetListOfEvents().remove(i);
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
