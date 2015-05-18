package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import lab.chabingba.telerikacademyschedule.Helpers.Constants;
import lab.chabingba.telerikacademyschedule.Helpers.Engine;

public class MainMenuActivity extends Activity {
    private ArrayList<Event> listOfOldEvents = Data.GetListOfOldEvents();
    private ArrayList<Event> listOfEvents = Data.GetListOfEvents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        Engine.StartEngine(this, Data.GetOutputFile(), listOfEvents, Data.GetOutputFileForOldEvents(), listOfOldEvents);

        Button bCurrentEvents = (Button) findViewById(R.id.bCurrentEvents);
        Button bOldEvents = (Button) findViewById(R.id.bOldEvents);
        Button bOptions = (Button) findViewById(R.id.bOptions);
        Button bExit = (Button) findViewById(R.id.bMainExit);
    }

    public void StartCurrentEvents(View v) {
        startActivity(new Intent(this, CurrentEventsActivity.class));
    }

    public void StartOldEvents(View v) {
        startActivity(new Intent(this, OldEventsActivity.class));
    }

    public void StartOptions(View v) {
        CharSequence message = "Coming soon.";
        Toast toast;
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void Exit(View v) {
        finish();
    }
}