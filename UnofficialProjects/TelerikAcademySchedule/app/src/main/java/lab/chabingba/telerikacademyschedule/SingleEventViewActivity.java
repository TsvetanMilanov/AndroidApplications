package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SingleEventViewActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_view);

        final Event event;

        event = (Event) getIntent().getSerializableExtra("BundleEvent");

        TextView textViewType = (TextView) findViewById(R.id.tvType);

        textViewType.setText(event.GetEventType());

        TextView textViewDate = (TextView) findViewById(R.id.tvDate);

        textViewDate.setText(event.GetEventDate());

        TextView textViewHour = (TextView) findViewById(R.id.tvHour);

        textViewHour.setText(event.GetEventHour());

        TextView textViewDescription = (TextView) findViewById(R.id.tvDescription);

        textViewDescription.setText(event.GetEventDescription());

        Button editButton = (Button) findViewById(R.id.buttonEdit);

        CheckBox cbIsFinished = (CheckBox) findViewById(R.id.cbFinished);

        cbIsFinished.setEnabled(false);

        cbIsFinished.setChecked(event.GetIsFinished());

        if (event.GetIsFinished()) {
            editButton.setVisibility(View.INVISIBLE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SingleEventViewActivity.this, SingleEventEditActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("Event", event);

                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }
        });

        Button removeButton = (Button) findViewById(R.id.buttonRemove);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Data.GetListOfEvents().size(); i++) {
                    Event currentEvent = Data.GetListOfEvents().get(i);

                    if (currentEvent.GetEventID() == event.GetEventID()) {
                        Data.GetListOfEvents().remove(i);
                        break;
                    }
                }
                Intent setIntent = new Intent(SingleEventViewActivity.this, CurrentEventsActivity.class);
                startActivity(setIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Event event;
        event = (Event) getIntent().getSerializableExtra("BundleEvent");

        Intent setIntent;

        if (event.GetIsFinished()) {
            setIntent = new Intent(SingleEventViewActivity.this, OldEventsActivity.class);
        } else {
            setIntent = new Intent(SingleEventViewActivity.this, CurrentEventsActivity.class);
        }
        startActivity(setIntent);
        finish();
    }
}
