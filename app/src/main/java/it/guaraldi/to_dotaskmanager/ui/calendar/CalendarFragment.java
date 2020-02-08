package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.utils.DateUtils;


public class CalendarFragment extends BaseFragment implements CalendarContract.View, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    @Inject CalendarPresenter mPresenter;
    private static final String TAG = "CalendarFragment";
    private AppBarLayout appBarLayout;
    private CompactCalendarView compactCalendarView;
    private boolean isExpanded = false;
    private WeekView mWeekView;
    private WeekView.EventClickListener mEventClickListener;
    private WeekView.EventLongPressListener mEventLongPressListener;
    private MonthLoader.MonthChangeListener mMonthChangeListener;
    private Intent mIntent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        mPresenter.attachView(this);
        return inflater.inflate(R.layout.calendar_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if( (mIntent =(getActivity().getIntent())) != null){
            Log.d(TAG, "onViewCreated: mIntentData ="+mIntent.getExtras());
            if(mIntent.getAction() == Const.DETAILS_TASK_F)
                mPresenter.openTaskDetails(mIntent.getBundleExtra(Const.TASK_DATA));
            if(mIntent.getAction() == Const.POSTPONE_TASK_F)
                mPresenter.openEditTask(mIntent.getBundleExtra(Const.TASK_DATA));
        }
        mPresenter.attachView(this);
        mPresenter.checkSession();
    }

    @Override
    public void onStart() {
        super.onStart();



    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getSizeTableTasks();
        mPresenter.getLastId();
        mPresenter.printAllTasks();


//// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
//            @Override
//            public void onEventClick(WeekViewEvent event, RectF eventRect) {
//                Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_taskDetailsFragment);
//            }
//        });
//
//// The week view has infinite scrolling horizontally. We have to provide the events of a
//// month every time the month changes on the week view.
//        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
//            @Override
//            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//                return null;
//            }
//        });
//
//// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);
    }

    @Override
    public void onStop() {
        super.onStop();

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
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            mPresenter.signOut();

        } else if (id == R.id.nav_gallery) {
            mPresenter.deleteAllTasks();
        } else if (id == R.id.nav_slideshow) {

            Calendar start = createCalendar(27,Calendar.FEBRUARY,2020,23,50);
            Calendar end = createCalendar(27, Calendar.FEBRUARY,2020,23,55);
            Calendar startFirstDay = copyCalendar(start);
            startFirstDay.set(Calendar.DAY_OF_MONTH,1);
            Calendar endDay = createCalendar(31,Calendar.MARCH,2021,0,0);
            Task task = fakeTask(start.getTimeInMillis(),end.getTimeInMillis());
            String groupId = UUID.randomUUID().toString();
            pippo(task,new int[]{Calendar.MONDAY,Calendar.WEDNESDAY,Calendar.FRIDAY},endDay,groupId);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
    public void updateData() {

    }

    @Override
    public void updateSession(String displayName,String email, int result) {
        if(result > 0){
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_edit_task:
                    mPresenter.addNewTask();
                break;
            case R.id.left_fab:
                Navigation.findNavController(getView()).navigate(R.id.action_calendarFragment_to_loginFragment);
                break;
        }
    }

    @Override
    protected void initViews() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        FloatingActionButton fab2 = (FloatingActionButton)getActivity().findViewById(R.id.left_fab);
        fab.setOnClickListener(this);
        fab2.setOnClickListener(this);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        appBarLayout = getActivity().findViewById(R.id.app_bar_layout);
        //CompactCalendarView
        compactCalendarView = getActivity().findViewById(R.id.compactcalendar_view);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mPresenter.getMonthTasks(Calendar.getInstance());
                setSubtitle(DateUtils.formatMonthTitle(firstDayOfNewMonth));
            }
        });
        ImageView arrow = getActivity().findViewById(R.id.date_picker_arrow);
        RelativeLayout datePickerButton = getActivity().findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(v -> {
            float rotation = isExpanded ? 0 : 180;
            ViewCompat.animate(arrow).rotation(rotation).start();
            isExpanded = !isExpanded;
            appBarLayout.setExpanded(isExpanded, true);

        });

    }

    @Override
    protected void setUp(Bundle data) {
        if(data != null)
            Log.d(TAG, "setUp: data not null");
        compactCalendarView.setLocale(TimeZone.getDefault(),Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) getActivity().findViewById(R.id.weekView);

        setCurrentDate(new Date());
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.calendar_fragment_nav_header);
        TextView navHeaderUserName= navHeaderView.findViewById(R.id.nav_header_username);
        TextView navHeaderEmail = navHeaderView.findViewById(R.id.nav_header_email);
        navHeaderUserName.setText(data.getString("USERNAME"));
        navHeaderEmail.setText(data.getString("EMAIL"));
//        startNotificationService();
    }


    private void setCurrentDate(Date date) {
        setSubtitle(DateUtils.formatMonthTitle(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
            mPresenter.getMonthTasks(Calendar.getInstance());
//            Calendar c = Calendar.getInstance();
//            c.setTime(date);
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//
//            mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
//                @Override
//                public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
//                    Calendar start = Calendar.getInstance();
//                    Calendar end = Calendar.getInstance();
//                    start.setTimeInMillis(System.currentTimeMillis());
//                    end.setTimeInMillis(System.currentTimeMillis()+ 1000 * 60 * 30);
//                    WeekViewEvent event = new WeekViewEvent(0,"title","location",start,end);
//                    List<WeekViewEvent> events = new ArrayList<>();
//                    events.add(event);
//                    return events;
//                }
//            });
        }
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = getActivity().findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }

    private Task fakeTask(long startDate, long endDate){
        String taskId = "0";
        String groupId = taskId;
        String title = "title";
        String email = "email";
        boolean allDay = false;
        int priority = 3;
        String category = "family";
        String start = String.valueOf(startDate);
        String end = String.valueOf(endDate);
        String description = "description";
        String color = "color";
        return new Task(taskId,groupId, title, email, allDay, priority, category,
                start,end,description,color);
    }
    private void cacca(){

    }


    private Task createNewTask(Task task,String groupId,long startTime,long endTime){
        String startDate = String.valueOf(startTime);
        String endDate = String.valueOf(endTime);

        return new Task(task.getId(),task.getTitle(),task.getEmail(),task.isAllDay(),groupId,task.getPriority(),
                task.getCategory(),task.getStatus(),startDate,endDate,task.getLongitude(),task.getLatitude(),
                task.getDescription(),task.getColor());
    }
    private void modifyCalendar(Calendar c,int day, int month, int year,int hour, int minute){
        if(day!=-1)
            c.set(Calendar.DAY_OF_MONTH,day);
        if(month!=-1)
            c.set(Calendar.MONTH,month);
        if(year!=-1)
            c.set(Calendar.YEAR,year);
        if(hour!=-1)
            c.set(Calendar.HOUR_OF_DAY,hour);
        if(minute!=-1)
            c.set(Calendar.MINUTE,minute);
    }
    private int getDifference(Calendar smaller, Calendar bigger){
        int smallerDom = smaller.get(Calendar.DAY_OF_MONTH);
        int biggerDom = bigger.get(Calendar.DAY_OF_MONTH);

        int smallerMonth = smaller.get(Calendar.MONTH);
        int biggerMonth = bigger.get(Calendar.MONTH);

        int smallerYear = smaller.get(Calendar.YEAR);
        int biggerYear = bigger.get(Calendar.YEAR);

        if(smallerMonth == biggerMonth)
            return biggerDom - smallerDom;

        else{

            int numberMonths = biggerYear>smallerYear ? 12 + biggerMonth - smallerMonth:
                    biggerMonth - smallerMonth;
            int numberOfDays = 0;

            for(int i = 0 ; i < numberMonths; i++){
                if(i==0)
                    numberOfDays = getLastDayOfMonth(smallerMonth, smaller.get(Calendar.YEAR)) - smallerDom;
                else
                    numberOfDays += smallerMonth+i == Calendar.JANUARY ?
                                getLastDayOfMonth(smallerMonth + i, biggerYear):
                                getLastDayOfMonth(smallerMonth + i, smallerYear);
                if(i+1 == numberMonths)
                    numberOfDays += biggerDom;
            }
            return numberOfDays;
        }
    }
    private int getLastDayOfMonth(int month, int year){
        int result = -1;
        switch (month){
            case Calendar.NOVEMBER:
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
                result = 30;
                break;
            case Calendar.FEBRUARY:
                if((year%4 == 0 && year%100 == 0 && year%400 == 0) || (year%4==0 && year%100!=0))
                    result = 29;
                else
                    result = 28;
                break;
            default:
                result = 31;
        }
        return result;
    }
    private void calculateTheDayRepetition(Task task, String endDayTv, int repeatEveryNumber, String periodName,
                                           boolean [] daysOfWeek){

        Calendar startDay = DateUtils.stringToCalendarCompleteDate(task.getStart());
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE,0);
        long dayDuration = 60*60*24*1000;
        Log.d(TAG, "calculateTheDayRepetition: ");
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(DateUtils.parseLastDay(endDayTv));
        endDay.set(Calendar.HOUR_OF_DAY,0);
        endDay.set(Calendar.MINUTE,0);
        endDay.setTimeInMillis(endDay.getTimeInMillis()+dayDuration);

        Log.d(TAG, "createRepeatedPeriodText: endDay --> " +
                "day = "+endDay.get(Calendar.DAY_OF_MONTH)+
                "month ="+endDay.get(Calendar.MONTH)+
                "year = "+endDay.get(Calendar.YEAR)+
                "hour = "+endDay.get(Calendar.HOUR_OF_DAY)+
                "minutes = "+endDay.get(Calendar.MINUTE));


        Log.d(TAG, "createRepeatedPeriodText: AFTER ADDED ONE DAY endDay --> " +
                "day = "+endDay.get(Calendar.DAY_OF_MONTH)+
                "month ="+endDay.get(Calendar.MONTH)+
                "year = "+endDay.get(Calendar.YEAR)+
                "hour = "+endDay.get(Calendar.HOUR_OF_DAY)+
                "minutes = "+endDay.get(Calendar.MINUTE));


//        List<Task> tasks = new ArrayList<>();
//
//        switch (periodName){
//            case "day":
//            case "days":
//                long difference = endDay.getTimeInMillis() - startDay.getTimeInMillis();
//                int numberDaysOfPeriod = (int) (difference / dayDuration);
//                int daysWithoutEvent = repeatEveryNumber -1;
//                tasks.addAll(calculateTasksForEveryDay(numberDaysOfPeriod,repeatEveryNumber,
//                        repeatEveryNumber-1,task,startDay.getTimeInMillis()));
//                break;
//            case "week":
//            case "weeks":
//                int [] daysSelected = calculateDaySelected(daysOfWeek);
//                int occurence = (int) (endDay.getTimeInMillis() - startDay.getTimeInMillis()) + 1;
//                for(int i = 0; i<daysSelected.length; i++){
//                    int dow = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart())).get(Calendar.DAY_OF_WEEK);
//                    tasks.addAll(calculateTasksForWeekDays(daysSelected,i,dow,occurence,startDay.getTimeInMillis(),
//                            endDay.getTimeInMillis(),task));
//                }
//                break;
//            case "month":
//            case "months":
//
//
//
//                break;
//            case "year":
//            case "years":
//                break;
//
//        }




    }


    private List<Task> calculateTasksForEveryDay(int numberDaysOfPeriod, int repeatEveryNumber,int daysWithoutEvent ,
                                                 Task task, long startDay ){
//        List<Task> tasks = new ArrayList<>();
//        if(numberDaysOfPeriod > repeatEveryNumber){
//            int occurence = numberDaysOfPeriod % repeatEveryNumber !=0 ? numberDaysOfPeriod/repeatEveryNumber +1 :
//                    numberDaysOfPeriod/repeatEveryNumber;
//
//
//            for(int i =0; i<occurence; i++){
//
//                String startDate = i==0 ? task.getStart() :
//                        String.valueOf(Long.parseLong(task.getStart()) + (daysWithoutEvent * dayDuration * i));
//                String endDate;
//
//                if(!task.isAllDay())
//                    endDate = i==0 ? task.getEnd() :
//                            String.valueOf(Long.parseLong(task.getEnd()) + (daysWithoutEvent * dayDuration * i));
//                else
//                    endDate = i==0 ? String.valueOf(startDay+ dayDuration -1) :
//                            String.valueOf( startDay+ dayDuration -1 + (daysWithoutEvent * dayDuration * i));
//
//                Task newTask = new Task(task.getId(),task.getTitle(),task.getEmail(),task.isAllDay(),task.getGroupId(),
//                        task.getPriority(),task.getCategory(),task.getStatus(),startDate,endDate,
//                        task.getLongitude(),task.getLatitude(),task.getDescription(),task.getColor());
//
//                Log.d(TAG, "createRepeatedPeriodText: TASK START DATE = "+DateUtils.stringToCalendarCompleteDate(newTask.getStart()).getTime());
//                Log.d(TAG, "createRepeatedPeriodText: TASK END DATE = "+DateUtils.stringToCalendarCompleteDate(newTask.getEnd()).getTime());
//                tasks.add(newTask);
//            }
//        }
//        return tasks;
        return null;
    }

    private Calendar createCalendar(int day, int month, int year,int hour,int minute){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);

        return c;
    }
    private Calendar copyCalendar(Calendar calendar){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        c.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        return c;
    }

    private List<Task> pippo(Task task, int [] daysOfWeek, Calendar endDayCal, String groupId){
        List<Task> tasks = new ArrayList<>();
        Calendar sCal;
        Calendar eCal;
        Calendar fCal    = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));

        // set time of task
        if(!task.isAllDay()){
            sCal =  copyCalendar(fCal);
            eCal = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }else {
            sCal = copyCalendar(fCal);
            eCal = copyCalendar(fCal);
            modifyCalendar(sCal,-1,-1,-1,0,0);
            modifyCalendar(eCal,-1,-1,-1,23,59);
        }

        modifyCalendar(fCal,1,-1,-1,0,0);


        //first day of calendar
        int fDay   = fCal.get(Calendar.DAY_OF_MONTH);
        int fMonth = fCal.get(Calendar.MONTH);
        int fYear  = fCal.get(Calendar.YEAR);
        int fLastDOM = getLastDayOfMonth(fMonth,fYear);

        //start date task
        int sDay;
        int sMonth = fMonth;
        int sYear = fYear;

        //data for calculate difference between 2 days of week
        int idDOWTask= 0;
        int gap  = calculateGapBetweenDOWs(daysOfWeek[idDOWTask],fCal);

        //calculate cycles
        int endDayMonth = endDayCal.get(Calendar.MONTH);
        int totalMonths = fYear == endDayMonth ? endDayMonth - fMonth : endDayMonth + Calendar.DECEMBER - fMonth;
        int totalCycles= calculateTotalCiclesOfSumMonths(totalMonths,fMonth,fYear);

        long taskTime = sCal.getTimeInMillis();
        long endTime = endDayCal.getTimeInMillis();
        for(int i = 0; i<=totalCycles; i++){

            Log.d(TAG, "pippo: i="+i+" fCal="+fCal.getTime());
            if(fDay+gap>fLastDOM){
                sDay  =  fDay + gap - fLastDOM;
                sYear = fMonth + 1 > Calendar.DECEMBER ? fYear+1 : fYear;
                sMonth = fMonth + 1 > Calendar.DECEMBER ? fMonth + 1 - Calendar.UNDECIMBER : fMonth+1;
            }
            else
                sDay = fDay + gap;
            modifyCalendar(sCal,sDay,sMonth,sYear,-1,-1);
            modifyCalendar(eCal,sDay,sMonth,sYear,-1,-1);
            Log.d(TAG, "pippo: gap="+gap+" SET sCal="+sCal.getTime());
            if(sCal.get(Calendar.DAY_OF_WEEK) == daysOfWeek[idDOWTask]
                    && sCal.getTimeInMillis() >= taskTime
                    && sCal.getTimeInMillis() <= endTime) {
                tasks.add(createNewTask(task, groupId, sCal.getTimeInMillis(), eCal.getTimeInMillis()));
                Log.d(TAG, "pippo: ADDED sCal=" + sCal.getTime());
                //looking for other days of week
                if(idDOWTask+1 < daysOfWeek.length) {
                    idDOWTask++;
                    gap = calculateGapBetweenDOWs(daysOfWeek[idDOWTask],fCal);
                    i--; //for every Day Selected in this Week

                    continue;
                }
            }

            Log.d(TAG, "pippo: FINISH THE DAY IN THIS WEEK!!!");

            if(fDay + 7 > fLastDOM){
                fDay += 7 -fLastDOM;
                fYear += fMonth+1 > Calendar.DECEMBER ? 1 : 0;
                fMonth += fMonth+1 > Calendar.DECEMBER ? 1 - Calendar.UNDECIMBER : 1;
                fLastDOM = getLastDayOfMonth(fMonth,fYear);
            }
            else
                fDay += 7;
            modifyCalendar(fCal,fDay,fMonth,fYear,-1,-1);
            idDOWTask = 0;
            gap = calculateGapBetweenDOWs(daysOfWeek[idDOWTask],fCal);
            Log.d(TAG, "pippo: UPDATE FIRST FOR NEXT SEARCH");
            Log.d(TAG, "pippo: fCal="+fCal.getTime());
        }
        for (Task t: tasks)
            Log.d(TAG, "pippo: t="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return tasks;
    }

    private int calculateGapBetweenDOWs(int tDOW, Calendar fCal){
        return tDOW - fCal.get(Calendar.DAY_OF_WEEK);
    }
    private int calculateTotalCiclesOfSumMonths(int totalMonths,int startMonth, int startYear){
        int result = 0;
        for(int i= 0; i<=totalMonths; i++){
            int year =  startMonth+i > Calendar.DECEMBER ? startYear+1: startYear;
            int month = startMonth+i > Calendar.DECEMBER ? startMonth+i - Calendar.UNDECIMBER : startMonth+i;
            result += getLastDayOfMonth(month,year);
        }
        return result;
    }



}
