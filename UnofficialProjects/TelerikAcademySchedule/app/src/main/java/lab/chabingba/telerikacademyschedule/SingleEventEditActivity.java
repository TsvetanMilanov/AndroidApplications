package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lab.chabingba.telerikacademyschedule.Helpers.Constants;

import static lab.chabingba.telerikacademyschedule.Helpers.Constants.EventType.values;

/**
 * Created by Tsvetan on 2015-04-13.
 */
public class SingleEventEditActivity extends Activity {
    private Event event = new Event();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_edit_view);

        this.event = (Event) getIntent().getSerializableExtra("Event");

        TextView textViewName = (TextView) findViewById(R.id.tvName);

        final EditText etName = (EditText) findViewById(R.id.etName);

        etName.setVisibility(View.INVISIBLE);

        final Spinner spinnerType = (Spinner) findViewById(R.id.spinnerType);

        Constants.EventType[] allEventTypes = values();

        final String[] spinnerTypeItems = CreateOptionsForType(allEventTypes);

        ArrayAdapter<String> adapterForEventType = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerTypeItems);

        spinnerType.setAdapter(adapterForEventType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etName.setText(spinnerTypeItems[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etName.setText(spinnerTypeItems[0]);
            }
        });

        TextView textViewDate = (TextView) findViewById(R.id.tvDate);

        final EditText etDate = (EditText) findViewById(R.id.etDate);

        etDate.setText(event.GetEventDate());

        TextView textViewHour = (TextView) findViewById(R.id.tvHour);

        final EditText etHour = (EditText) findViewById(R.id.etHour);

        etHour.setText(event.GetEventHour());

        TextView textViewDescription = (TextView) findViewById(R.id.tvDescription);

        final EditText etDescription = (EditText) findViewById(R.id.etDescription);

        etDescription.setText(event.GetEventDescription());

        final CheckBox cbEditIsFinished = (CheckBox) findViewById(R.id.cbEditIsFinished);

        cbEditIsFinished.setChecked(event.GetIsFinished());

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
                    editedEvent.SetIsFinished(cbEditIsFinished.isChecked());

                } else {
                    throw new IllegalArgumentException("Field cannot be empty!");
                }

                Intent intent = new Intent(SingleEventEditActivity.this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Event", editedEvent);

                intent.putExtras(bundle);

                Data.GetListOfEvents().add(editedEvent.GetEventID(), editedEvent);
                Data.GetListOfEvents().remove(editedEvent.GetEventID() + 1);
                startActivity(intent);
                finish();
            }
        });
    }

    private String[] CreateOptionsForType(Constants.EventType[] allEventTypes) {
        String[] result = new String[allEventTypes.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = ConvertTypeFromEnumToString(allEventTypes[i]);
        }

        result = SetFirstEventType(result);

        return result;
    }

    private String[] SetFirstEventType(String[] result) {

        String currentEventType = this.event.GetEventName();

        for (int i = 0; i < result.length; i++) {
            String currentType = result[i];

            if (currentEventType.equals(currentType)) {
                String temp = result[0];
                result[0] = currentEventType;
                result[i] = temp;
            }

        }

        return result;
    }

    private String ConvertTypeFromEnumToString(Constants.EventType eventType) {
        String result = null;

        switch (eventType) {
            case Lecture:
                result = "Lecture";
                break;
            case Seminar:
                result = "Seminar";
                break;
            case Workshop:
                result = "Workshop";
                break;
            case Non_Technical_Lecture:
                result = "Non - Technical Lecture";
                break;
            case Exam:
                result = "Exam";
                break;
            default:
                result = "Null";
                break;
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(SingleEventEditActivity.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}