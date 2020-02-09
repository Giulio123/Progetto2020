package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.LocalDate;

import it.guaraldi.to_dotaskmanager.R;

public class CalendarDayView extends ViewContainer {

    TextView textView;
    CalendarDay day; // Will be set when this container is bound.
    ConstraintLayout layout;
    View flightTopView;
    View flightBottomView;
    CalendarView calendarView;

    public CalendarDayView(View view, CalendarView calendarView, CalendarFragment calendarFragment) {
        super(view);
        this.calendarView = calendarView;
        view.setOnClickListener(view1 -> {
            if (day.getOwner() == DayOwner.THIS_MONTH) {
                if (CalendarFragment.selectedDate != day.getDate()) {
                    LocalDate oldDate = CalendarFragment.selectedDate;
                    CalendarFragment.selectedDate = day.getDate();
                    calendarView.notifyDateChanged(day.getDate());
                    // TODO Controllare traduzione: oldDate?.let { calendarView.notifyDateChanged(it) }
                    if (oldDate != null) {
                        calendarView.notifyDateChanged(oldDate);
                    }
                    calendarFragment.updateAdapterForDate(day.getDate());
                    // Aggiornare adapter per lista tasl
                    // updateAdapterForDate(day.date);
                }
            }
        });
        // calendarView = view.findViewById(R.id.calendarView);
        textView = view.findViewById(R.id.calendarDayText);
        layout = view.findViewById(R.id.exFiveDayLayout);
        flightTopView = view.findViewById(R.id.exFiveDayFlightTop);
        flightBottomView = view.findViewById(R.id.exFiveDayFlightBottom);
    }

}