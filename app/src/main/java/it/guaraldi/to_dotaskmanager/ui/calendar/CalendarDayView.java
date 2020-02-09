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
    View taskBar1;
    View taskBar2;
    View taskBar3;
    View taskBar4;
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
        taskBar1 = view.findViewById(R.id.taskBar1);
        taskBar2 = view.findViewById(R.id.taskBar2);
        taskBar3 = view.findViewById(R.id.taskBar3);
        taskBar4 = view.findViewById(R.id.taskBar4);
    }

    public void unsetBar() {
        taskBar1.setBackground(null);
        taskBar2.setBackground(null);
        taskBar3.setBackground(null);
        taskBar4.setBackground(null);
    }

    public View getBar(int i) {
        View toReturn = taskBar1;
        switch (i) {
            case 0:
                toReturn = taskBar1;
                break;
            case 1:
                toReturn = taskBar2;
                break;
            case 2:
                toReturn = taskBar3;
                break;
            case 3:
                toReturn = taskBar4;
                break;
        }
        return toReturn;
    }
}