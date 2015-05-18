package lab.chabingba.telerikacademyschedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tsvetan on 2015-05-15.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(Context context, String[] events) {

        super(context, R.layout.custom_row, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater.from(getContext()));

        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);

        String[] eventAsStringArray = getItem(position).split("\r\n");

        String eventType = eventAsStringArray[0];
        String eventDate = eventAsStringArray[1];
        String eventDay = eventAsStringArray[2];
        String eventHour = eventAsStringArray[3];

        TextView menuItemType = (TextView) customView.findViewById(R.id.tvMenuItemType);
        TextView menuItemDate = (TextView) customView.findViewById(R.id.tvMenuItemDate);
        TextView menuItemDay = (TextView) customView.findViewById(R.id.tvMenuItemDay);
        TextView menuItemHour = (TextView) customView.findViewById(R.id.tvMenuItemHour);

        menuItemType.setText(eventType);
        menuItemDate.setText(eventDate);
        menuItemDay.setText(eventDay);
        menuItemHour.setText(eventHour);

        ImageView ivIcon = (ImageView) customView.findViewById(R.id.imageView);

        int resID = SelectIcon(eventType);

        ivIcon.setImageResource(resID);

        return customView;
    }

    private int SelectIcon(String eventType) {

        /*The default case is to avoid force close and to support types before creating enum with types*/

        switch (eventType) {
            case "Lecture":
                return R.drawable.lecture_icon_png;
            case "Seminar":
                return R.drawable.seminar_icon_png;
            case "Workshop":
                return R.drawable.workshop_icon_png;
            case "Non - Technical Lecture":
                return R.drawable.nt_lecture_icon_png;
            case "Exam":
                return R.drawable.exam_icon_png;
            default:
                if (eventType.contains("exam")) {
                    return R.drawable.exam_icon_png;
                } else if (eventType.equals("Non - technical Lecture")) {
                    return R.drawable.nt_lecture_icon_png;
                }
                return R.drawable.icon;
        }
    }
}
