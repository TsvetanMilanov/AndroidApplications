package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tsvetan on 2015-04-13.
 */
public class SingleEventEdit extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_edit_view);

        final Event event = (Event) getIntent().getSerializableExtra("Event");

        TextView textViewName = (TextView) findViewById(R.id.tvName);

        final EditText etName = (EditText) findViewById(R.id.etName);

        etName.setText(event.GetEventName());

        TextView textViewDate = (TextView) findViewById(R.id.tvDate);

        final EditText etDate = (EditText) findViewById(R.id.etDate);

        etDate.setText(event.GetEventDate());

        TextView textViewHour = (TextView) findViewById(R.id.tvHour);

        final EditText etHour = (EditText) findViewById(R.id.etHour);

        etHour.setText(event.GetEventHour());

        TextView textViewDescription = (TextView) findViewById(R.id.tvDescription);

        final EditText etDescription = (EditText) findViewById(R.id.etDescription);

        etDescription.setText(event.GetEventDescription());

        Button doneButton = (Button) findViewById(R.id.buttonDone);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event editedEvent = new Event();

                if (etName.getText().length() > 0 && etDate.getText().length() > 0 && etHour.getText().length() > 0 && etDescription.getText().length() > 0) {
                    editedEvent.SetEventID(event.GetEventID());
                    editedEvent.SetEventName(etName.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    Calendar date = Calendar.getInstance();
                    try {
                        date.setTime(dateFormat.parse(etDate.getText().toString()));
                    } catch (ParseException e) {
                        Log.e("DATE", e.getMessage());
                    }
                    editedEvent.SetEventDate(date);
                    editedEvent.SetEventHour(etHour.getText().toString());
                    editedEvent.SetEventDescription(etDescription.getText().toString());

                } else {
                    throw new IllegalArgumentException("Field cannot be empty!");
                }

                Class nextClass;

                try {
                    nextClass = Class.forName("lab.chabingba.telerikacademyschedule.MainActivity");

                    Intent intent = new Intent(SingleEventEdit.this, nextClass);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Event", editedEvent);

                    intent.putExtras(bundle);

                    Data.listOfEvents.add(editedEvent.GetEventID(), editedEvent);
                    Data.listOfEvents.remove(editedEvent.GetEventID() + 1);
                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(SingleEventEdit.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}