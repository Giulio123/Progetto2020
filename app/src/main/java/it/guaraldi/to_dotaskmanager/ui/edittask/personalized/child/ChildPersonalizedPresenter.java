package it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.LocalDataSource;
import it.guaraldi.to_dotaskmanager.data.remote.RemoteDataSource;

import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.util.DateUtils;

public class ChildPersonalizedPresenter extends BasePresenter<ChildPersonalizedContract.View> implements ChildPersonalizedContract.Presenter {

    private TasksRepository mRepository;
    private static final String TAG = "ChildPersonalizedPresen";
    @Inject
    public ChildPersonalizedPresenter(TasksRepository repository) {
        mRepository = repository;
    }

    @Override
    public void setUpLastDay(String date) {
        Calendar c = DateUtils.stringToCalendarDate(date);
        c.set(Calendar.YEAR,c.get(Calendar.YEAR) + 1);
        c.set(Calendar.MONTH,c.get(Calendar.MONTH) + 1);
        c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH));
        mView.loadLastDay(DateUtils.calendarToStringLastDay(c));
    }

    @Override
    public void updateLastDay(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        mView.loadLastDay(DateUtils.calendarToStringLastDay(c));
    }

    @Override
    public void updateCurrentDay(String date) {
        Calendar c = DateUtils.stringToCalendarDate(date);
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                mView.loadCurrentDay(0);
                break;
            case Calendar.TUESDAY:
                mView.loadCurrentDay(1);
                break;
            case Calendar.WEDNESDAY:
                mView.loadCurrentDay(2);
                break;
            case Calendar.THURSDAY:
                mView.loadCurrentDay(3);
                break;
            case Calendar.FRIDAY:
                mView.loadCurrentDay(4);
                break;
            case Calendar.SATURDAY:
                mView.loadCurrentDay(5);
                break;
            case Calendar.SUNDAY:
                mView.loadCurrentDay(6);
                break;
        }
    }

    @Override
    public void calculateMonthOccurrences(String date,String prefix,String [] nameOccurrences) {
        Calendar c = DateUtils.stringToCalendarDate(date);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.ENGLISH);
        List<String> weekOfMonth = getWeekOfMonth(c,nameOccurrences);
        String [] result = weekOfMonth.get(1).equals("") ?new String[]{prefix+", "+"Day "+dayOfMonth,
                prefix+", "+"the "+weekOfMonth.get(0)+" "+dayOfWeek} :
                new String[]{prefix+", "+"Day "+dayOfMonth, prefix+", "+"the "+weekOfMonth.get(0)+" "+dayOfWeek,
                "the "+weekOfMonth.get(1)+" "+dayOfWeek};
        mView.loadMonthSpinnerData(result);
    }



    private List<String> getWeekOfMonth(Calendar c, String [] nameOccurrences){
        List<String> result = new ArrayList<>();
        String weekOfMonthText = "";
        String lastWeekOfMonthText = "";
        int res = calculateDayOccurenceOfMonth(c);
        Log.d(TAG, "getWeekOfMonth: res ="+res);
        switch (res){
            case 0:
                weekOfMonthText = nameOccurrences[0];
                break;
            case 1:
                weekOfMonthText = nameOccurrences[1];
                break;
            case 2:
                weekOfMonthText = nameOccurrences[2];
                break;
            case 3:
                weekOfMonthText = nameOccurrences[3];
                if(checkLastDayOfMonth(c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH),c.get(Calendar.YEAR)))
                    lastWeekOfMonthText = nameOccurrences[4];
                break;
            case 4:
                weekOfMonthText = nameOccurrences[4];
                break;
        }
        result.add(weekOfMonthText);
        result.add(lastWeekOfMonthText);

        return result;
    }

    private boolean checkLastDayOfMonth(int day, int month, int year){
        boolean result;
        switch (month){
            case Calendar.NOVEMBER:
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
                result = day + 7 > 30;
            case Calendar.FEBRUARY:
                if((year%4 == 0 && year%100 == 0 && year%400 == 0) || (year%4==0 && year%100!=0))
                    result = day + 7 > 29;
                else
                    result = day + 7 > 28;
                break;
            default:
                result = day + 7 > 31;
                break;
        }
        return result;
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
    private Calendar firstDayCalendar(Calendar calendar){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        c.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH,1);
        c.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        return c;
    }

    private int calculateDayOccurenceOfMonth(Calendar taskDate){
        Log.d(TAG, "createCalendar: c="+taskDate.getTime());
        Calendar firstDayOfMonth = firstDayCalendar(taskDate);
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH,1);

        int dowFirst = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
        int dowTask = taskDate.get(Calendar.DAY_OF_WEEK);

        int domFirst = firstDayOfMonth.get(Calendar.DAY_OF_MONTH);
        int domTask = taskDate.get(Calendar.DAY_OF_MONTH);

        int gap = dowTask - dowFirst;

        Log.d(TAG, "GAP ="+gap+ " dowFirst="+dowFirst+" dowTask="+dowTask);
        for(int i = 0; i < 5; i++)
            if(domFirst + gap == domTask || domFirst + gap + 7*(i)==domTask)
                return gap>0 ? i:(i-1);

        return -1;
    }
}
