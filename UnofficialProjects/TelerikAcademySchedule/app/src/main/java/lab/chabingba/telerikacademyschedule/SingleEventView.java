package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SingleEventView extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_view);

        final Event event = (Event) getIntent().getSerializableExtra("BundleEvent");

        TextView textViewName = (TextView) findViewById(R.id.tvName);

        textViewName.append(event.GetEventName());

        TextView textViewDate = (TextView) findViewById(R.id.tvDate);

        textViewDate.append(event.GetEventDate());

        TextView textViewHour = (TextView) findViewById(R.id.tvHour);

        textViewHour.append(event.GetEventHour());

        TextView textViewDescription = (TextView) findViewById(R.id.tvDescription);

        textViewDescription.append("\r\n" + event.GetEventDescription());

        Button editButton = (Button) findViewById(R.id.buttonEdit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class nextClass;

                try {
                    nextClass = Class.forName("lab.chabingba.telerikacademyschedule.SingleEventEdit");

                    Intent intent = new Intent(SingleEventView.this, nextClass);

                    Bundle bundle = new Bundle();

                    Event eventToPass = event;

                    bundle.putSerializable("Event", eventToPass);

                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    Log.e("CLASS", e.getMessage());
                }
            }
        });

        Button removeButton = (Button) findViewById(R.id.buttonRemove);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Data.listOfEvents.size(); i++) {
                    Event currentEvent = Data.listOfEvents.get(i);

                    if (currentEvent.GetEventID() == event.GetEventID()) {
                        Data.listOfEvents.remove(i);
                        break;
                    }
                }
                Intent setIntent = new Intent(SingleEventView.this, MainActivity.class);
                startActivity(setIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(SingleEventView.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}
