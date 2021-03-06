package lab.chabingba.telerikacademyschedule;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import lab.chabingba.telerikacademyschedule.Helpers.Constants;
import lab.chabingba.telerikacademyschedule.Helpers.EventHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.UpdateHelpers;

/**
 * Created by Tsvetan on 2015-04-24.
 */
public class OldEventsActivity extends ListActivity {
    private OldEventsActivity contextOfOldEventsActivity;
    private ArrayList<Event> listOfOldEvents = Data.GetListOfOldEvents();
    private File outputFileForOldEvents = new File(Constants.FileDirectory, "OldEvents.txt");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(0, 130, 255));

        //Set the value of the Content variable to use from another classes.
        contextOfOldEventsActivity = this;

        /* Update the output.txt if an event was edited
        (on app start Data.listOfOldEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFileForOldEvents, listOfOldEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFileForOldEvents, listOfOldEvents);

        FileHelpers.ReadEventsFromFile(listOfOldEvents, outputFileForOldEvents);

        Collections.sort(listOfOldEvents);

        UpdateHelpers.UpdateIndexes(outputFileForOldEvents, listOfOldEvents);

        /* Create string array with all event names and dates as string for list items. */

        ArrayList<Event> reversedListOfOldEvents = listOfOldEvents;
        Collections.reverse(reversedListOfOldEvents);

        String[] eventsThumbnails = EventHelpers.CreateEventThumbnails(reversedListOfOldEvents);

        setListAdapter(new CustomAdapter(this, eventsThumbnails));
        registerForContextMenu(this.getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Event eventToOpen = listOfOldEvents.get(position);

        Intent intent1 = new Intent(OldEventsActivity.this, SingleEventViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("BundleEvent", eventToOpen);
        bundle.putSerializable("List", listOfOldEvents);

        intent1.putExtras(bundle);
        startActivity(intent1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater;
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_short_version, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clearOldEvents:
                Data.outputFileForOldEvents.delete();

                Data.GetListOfOldEvents().clear();

                try {
                    Data.GetOutputFileForOldEvents().createNewFile();
                } catch (IOException e) {
                    Log.e("FILE", "Can't create OldEvents.txt;");
                }

                Intent refreshOldEventsActivityIntent = new Intent(OldEventsActivity.this, OldEventsActivity.class);

                startActivity(refreshOldEventsActivityIntent);
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
                CharSequence toastText = "Can't edit old events.";
                Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.removeEvent:
                if (listOfOldEvents.size() == 1) {
                    Data.GetOutputFileForOldEvents().delete();
                    Data.GetListOfOldEvents().clear();
                    try {
                        Data.GetOutputFileForOldEvents().createNewFile();
                    } catch (IOException e) {
                        Log.e("FILE", "Can't create OldEvents.txt;");
                    }
                }

                for (int i = 0; i < Data.GetListOfOldEvents().size(); i++) {
                    Event currentEvent = Data.GetListOfOldEvents().get(i);

                    if (currentEvent.GetEventID() == listOfOldEvents.get(id).GetEventID()) {
                        Data.GetListOfOldEvents().remove(i);
                        break;
                    }
                }

                Data.SetListValuesOfOldEvents(listOfOldEvents);

                Intent setIntent = new Intent(OldEventsActivity.this, OldEventsActivity.class);
                startActivity(setIntent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onBackPressed() {
        finish();
    }

    public Context GetContext() {
        return this.contextOfOldEventsActivity;
    }
}
