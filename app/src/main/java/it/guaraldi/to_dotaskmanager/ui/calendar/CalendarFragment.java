package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;
import it.guaraldi.to_dotaskmanager.utils.DateUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static java.util.Locale.ENGLISH;


public class CalendarFragment extends BaseFragment implements  CalendarContract.View, TasksAdapter.OnItemSelected {

    @Inject CalendarPresenter mPresenter;
    private static final String TAG = "CalendarFragment";
    private AppBarLayout appBarLayout;

    private boolean isExpanded = false;
    private WeekView mWeekView;
    private WeekView.EventClickListener mEventClickListener;
    private WeekView.EventLongPressListener mEventLongPressListener;
    private MonthLoader.MonthChangeListener mMonthChangeListener;
    private Intent mIntent;
    private DayOfWeek[] daysOfWeek;
    static LocalDate selectedDate = null;
    private TasksAdapter tasksAdapter;
    private CalendarView calendarView;
    private Toolbar mToolbar;
    private List<Task> tasks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        mPresenter.attachView(this);

        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if( (mIntent =(getActivity().getIntent())) != null){
            Log.d(TAG, "onViewCreated: action="+mIntent.getAction()+" mIntentData ="+mIntent.getExtras());
            if(Const.DETAILS_TASK_F.equals(mIntent.getAction())||Const.STATUS_UPDATE.equals(mIntent.getAction()))
                mPresenter.openTaskDetails(mIntent.getExtras());
            if(mIntent.getAction().equals(Const.POSTPONE_TASK_F))
                mPresenter.openEditTask(mIntent.getExtras());
        }
        if(getArguments()!=null)
            mPresenter.openEditTask(getArguments());

        CalendarFragment.selectedDate = LocalDate.now();
        mPresenter.attachView(this);
        mPresenter.checkSession();
        mPresenter.updateMonthTask(selectedDate);
        mToolbar = getActivity().findViewById(R.id.toolbar_calendar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab_edit_task);
        floatingActionButton.setOnClickListener(v -> mPresenter.addNewTask());
        initCalendar();
    }



    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getSizeTableTasks();
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if(mPresenter != null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_graphic_fragment)
            Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_graphicFragment);
        return true;
    }

    @Override
    public void showCalendar() {

    }

    @Override
    public void showTimeTable() {

    }

    @Override
    public void showTasksOfMonth() {

    }

    @Override
    public void showTaskDetails(Bundle taskData) {
        Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_taskDetailsFragment,taskData);
    }

    @Override
    public void showEditTaskView(Bundle taskData) {
        Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_editTaskFragment,taskData);
    }

    @Override
    public void reloadActivity() {
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    @Override
    public void updateData(List<Task> tasks) {
        this.tasks = tasks;
        calendarView.notifyCalendarChanged();
        if (CalendarFragment.selectedDate != null) {
            calendarView.notifyDateChanged(CalendarFragment.selectedDate);
        }
    }

    private void updateAdapter() {

    }

    @Override
    public void updateSession(String displayName,String email, int result) {
        if(result > 0){
            Log.d(TAG, "updateSession: USERNAME:"+displayName+" email:"+email);
            Bundle data = new Bundle();
            data.putString("USERNAME",displayName);
            data.putString("EMAIL",email);
            initViews();
            setUp(data);
            setHasOptionsMenu(true);
        }
        else
            Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_loginFragment);
    }

    public static void setSelectedDate(LocalDate date) {
        CalendarFragment.selectedDate = date;
    }

    private void onClickCustom() {

    }

    private void initCalendar() {

        // TODO Move con method, this wil setUp RecyclerView
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.exFiveRv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        tasksAdapter = new TasksAdapter(new ArrayList<>());
        tasksAdapter.setOnItemSelectedListener(this);
        recyclerView.setAdapter(tasksAdapter);


        calendarView = getActivity().findViewById(R.id.calendarView);
        ImageView exFiveNextMonthImage = getActivity().findViewById(R.id.exFiveNextMonthImage);
        ImageView exFivePreviousMonthImage = getActivity().findViewById(R.id.exFivePreviousMonthImage);
        TextView exFiveMonthYearText = getActivity().findViewById(R.id.exFiveMonthYearText);

        daysOfWeek = this.daysOfWeekFromLocale();

        YearMonth currentMonth = YearMonth.now();
        calendarView.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek[0]);
        exFiveMonthYearText.setText(currentMonth.getMonth().getDisplayName(TextStyle.SHORT, ENGLISH) + " " + currentMonth.getYear());
        calendarView.scrollToMonth(currentMonth);
        /******* Roba molto BRUTTA **********/
        CalendarFragment calendarFragment = this;
        calendarView.setDayBinder(new DayBinder<CalendarDayView>() {
            @Override
            public CalendarDayView create(View view) {
                return new CalendarDayView(view, calendarView, calendarFragment);
            }

            @Override
            public void bind(CalendarDayView container, CalendarDay day) {
                container.day = day;
                TextView textView = container.textView;
                ConstraintLayout layout = container.layout;
                textView.setText(Integer.toString(day.getDate().getDayOfMonth()));

                container.unsetBar();
                if (day.getOwner() == DayOwner.THIS_MONTH) {
                    textView.setTextColor(getResources().getColor(R.color.example_5_text_grey));
                    if (CalendarFragment.selectedDate == day.getDate()) {
                        layout.setBackgroundResource(R.drawable.example_5_selected_bg);
                    } else {
                        layout.setBackgroundResource(0);
                    }
                    // TODO settare colore in base al colore del task
                    List<Task> localTasks = mPresenter.getTasksOfDay(day.getDate());
                    for (int i = 0; i < localTasks.size(); i++) {
                        Log.d(TAG, "bind: LOCALTASK COLOR="+localTasks.get(i).getColor());
                        container.getBar(i).setBackgroundColor(getContext().getColor(Integer.parseInt(localTasks.get(i).getColor())));
                        if (i == 3) break;
                    }
                } else {
                    textView.setTextColor(getResources().getColor(R.color.example_5_text_grey_light));
                    layout.setBackground(null);
                }
            }
        });


        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<CalendarMonthView>() {
            @Override
            public void bind(CalendarMonthView container, CalendarMonth month) {
                if (container.legendLayout.getTag() == null) {
                    container.legendLayout.setTag(month.getYearMonth());
                    container.legendLayout.getChildCount();
                    for (int i = 0; i < container.legendLayout.getChildCount(); i++) {
                        TextView tv = (TextView) container.legendLayout.getChildAt(i);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            tv.setText(daysOfWeek[i].getDisplayName(TextStyle.SHORT, ENGLISH).toUpperCase(ENGLISH));
                        }
                        tv.setTextColor(getResources().getColor(R.color.example_5_text_grey));
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                    }
                }
            }

            @Override
            public CalendarMonthView create(View view) {
                return new CalendarMonthView(view);
            }

        });
        calendarView.setMonthScrollListener(new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth month) {
                mPresenter.updateMonthTask(LocalDate.of(month.getYear(), month.getMonth(), 1));
                //TODO Cambiare il numero del mese nel nome
                String title = month.getMonth() + " " + month.getYear();
                exFiveMonthYearText.setText(title);

                // Setto sempre il primo giorno del mese
                CalendarFragment.selectedDate = LocalDate.of(month.getYear(), month.getMonth(), 1);
                calendarView.notifyDateChanged(CalendarFragment.selectedDate);
                updateAdapterForDate(CalendarFragment.selectedDate);

                //Unit.INSTANCE; unit = new Unit();
                return Unit.INSTANCE;
            }
        });

        exFiveNextMonthImage.setOnClickListener(view -> {
            if (calendarView.findFirstVisibleMonth() != null) {
                Log.d(TAG, "initCalendar: exFiveNextMonthImage");
                YearMonth nextMonth = calendarView.findFirstVisibleMonth().getYearMonth().plusMonths(1);
                calendarView.smoothScrollToMonth(nextMonth);
                exFiveMonthYearText.setText(nextMonth.getMonth().getDisplayName(TextStyle.SHORT, ENGLISH) + " " + nextMonth.getYear());
            }
        });


        exFivePreviousMonthImage.setOnClickListener(view -> {
            if (calendarView.findFirstVisibleMonth() != null) {
                Log.d(TAG, "initCalendar:  exFivePreviousMonthImage");
                YearMonth nextMonth = calendarView.findFirstVisibleMonth().getYearMonth().minusMonths(1);
                calendarView.smoothScrollToMonth(nextMonth);
                exFiveMonthYearText.setText(nextMonth.getMonth().getDisplayName(TextStyle.SHORT, ENGLISH) + " " + nextMonth.getYear());
            }
        });


        calendarView.notifyCalendarChanged();

    }

    @Override
    protected void setUp(Bundle data) {
        if(data != null)
            Log.d(TAG, "setUp: data not null");
        mWeekView = (WeekView) getActivity().findViewById(R.id.weekView);

        setCurrentDate(new Date());
    }


    private void setCurrentDate(Date date) {
        setSubtitle(DateUtils.formatMonthTitle(date));
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = getActivity().findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }

    // ToDO update event adapter!!!!!
    public void updateAdapterForDate(LocalDate date) {
        List<Task> filterdTasks = mPresenter.getTasksOfDay(date);
        tasksAdapter.setDataset(filterdTasks);
        tasksAdapter.notifyDataSetChanged();
    }


    private DayOfWeek[] daysOfWeekFromLocale() {
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        DayOfWeek[] daysOfWeek1 = DayOfWeek.values();
        // TODO ordinare per settimana, GG
//        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
//        if (firstDayOfWeek != DayOfWeek.MONDAY) {
//            val rhs = daysOfWeek1.sliceArray(firstDayOfWeek.ordinal.daysOfWeek1.indices.last)
//            val lhs = daysOfWeek1.sliceArray(0 until firstDayOfWeek.ordinal)
//            daysOfWeek1 = rhs + lhs;
//        }
        return daysOfWeek1;
    }

    @Override
    public void getTaskIdOfItemSelected(int taskId) {
        Bundle data = new Bundle();
        data.putInt(ActivityUtils.ID_TASK, taskId);
        mPresenter.openTaskDetails(data);
    }
}
