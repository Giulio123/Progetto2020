package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.view.View;
import android.widget.LinearLayout;

import com.kizitonwose.calendarview.ui.ViewContainer;

import it.guaraldi.to_dotaskmanager.R;

public class CalendarMonthView extends ViewContainer {
    public LinearLayout legendLayout;

    public CalendarMonthView(View view) {
        super(view);
        legendLayout = view.findViewById(R.id.legendLayout);

    }
}
