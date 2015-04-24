package lab.chabingba.telerikacademyschedule;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import lab.chabingba.telerikacademyschedule.Helpers.Constants;
import lab.chabingba.telerikacademyschedule.Helpers.FileHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.ListActivityHelpers;
import lab.chabingba.telerikacademyschedule.Helpers.UpdateHelpers;

/**
 * Created by Tsvetan on 2015-04-24.
 */
public class OldEventsActivity extends ListActivity {
    public static OldEventsActivity contextOfOldEventsActivity;
    public ArrayList<Event> listOfOldEvents = Data.listOfOldEvents;
    private File outputFile = new File(Constants.TemplateFileDir, "OldEvents.txt");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.rgb(85, 255, 40));

        //Set the value of the Content variable to use from another classes.
        contextOfOldEventsActivity = this;

        /* Update the output.txt if an event was edited
        (on app start Data.listOfOldEvents is always empty, so only when adding event from edit to it it will have events in it.) */

        UpdateHelpers.UpdateOutputFile(outputFile, listOfOldEvents);

        //Update the indexes in output file
        UpdateHelpers.UpdateIndexes(outputFile, listOfOldEvents);

        FileHelpers.ReadEventsFromFile(listOfOldEvents, outputFile);

        Collections.sort(listOfOldEvents);

        UpdateHelpers.UpdateIndexes(outputFile, listOfOldEvents);

        /* Create string array with all event names and dates as string for list items. */
        String[] eventsThumbnails = ListActivityHelpers.CreateEventThumbnails(listOfOldEvents);

        setListAdapter(new ArrayAdapter<String>(OldEventsActivity.this, android.R.layout.simple_list_item_1, eventsThumbnails));
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
                Event eventToOpen = listOfOldEvents.get(id);

                    Intent intent1 = new Intent(OldEventsActivity.this, SingleEventEditActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Event", eventToOpen);
                    bundle.putSerializable("List", listOfOldEvents);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    finish();
                return true;
            case R.id.removeEvent:
                for (int i = 0; i < Data.listOfOldEvents.size(); i++) {
                    Event currentEvent = Data.listOfOldEvents.get(i);

                    if (currentEvent.GetEventID() == listOfOldEvents.get(id).GetEventID()) {
                        Data.listOfOldEvents.remove(i);
                        break;
                    }
                }
                Intent setIntent = new Intent(OldEventsActivity.this, OldEventsActivity.class);
                startActivity(setIntent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent setIntent = new Intent(OldEventsActivity.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}