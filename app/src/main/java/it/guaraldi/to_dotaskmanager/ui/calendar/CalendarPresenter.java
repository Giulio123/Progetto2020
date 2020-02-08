package it.guaraldi.to_dotaskmanager.ui.calendar;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.User;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarContract.Presenter;

public class CalendarPresenter extends BasePresenter<CalendarContract.View> implements CalendarContract.Presenter {
    private final TasksRepository mRepository;
    private static final String TAG = "CalendarPresenter";

    @Override
    public void checkViewAttached() {
        super.checkViewAttached();
    }

    @Inject
    public  CalendarPresenter(TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void addNewTask() {
        getView().showEditTaskView(null);
    }

    @Override
    public void checkSession() {
        //FAKE
//        mView.updateSession("Carmelo","carmelo@gmail.com",1);

        mRepository.getCurrentUser(new TasksDataSource.LoadSessionCallback() {
            @Override
            public void success(User user) {
                mView.updateSession(user.getDisplayName(),user.getEmail(),1);
                //TODO dati user+ caricare dati del calndario
            }

            @Override
            public void failure(Exception e) {
                mView.updateSession(null,null,0);
                //TODO schermata login per loggare un utente
            }
        });
    }

    @Override
    public void signOut() {
        mRepository.signOut(new TasksDataSource.SignOutCallback() {
            @Override
            public void success() {
                mView.reloadActivity();
            }
        });
    }

    @Override
    public void loadTasksOfMonth() {
    }

    @Override
    public void loadTasksForNewVisibleDays() {
    }

    @Override
    public void openTaskDetails(Bundle taskData) {

        mView.showTaskDetails(taskData);
    }

    @Override
    public void openEditTask(Bundle taskData) {
        Log.d(TAG, "openEditTask: ");
        mView.showEditTaskView(taskData);
    }

    @Override
    public void getSizeTableTasks() {
        mRepository.getSizeTableTaks(new TasksDataSource.DBCallbackSize() {
            @Override
            public void success(int sizeListTask) {
                Log.d(TAG, "success: SIZE TASKS="+sizeListTask);
            }
        });
    }


    @Override
    public void getLastId() {
        mRepository.getLastTaskId(new TasksDataSource.DBCallbackId() {
            @Override
            public void success(int lastId) {
                Log.d(TAG, "success: lastId ="+lastId);
            }
        });
    }

    @Override
    public void printAllTasks() {
        mRepository.getAllTasks(new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                for(Task t : tasks) {
                    Log.d(TAG, "PRINT ALL TASKS success: "+t.toString());
                }
            }
        });
    }

    @Override
    public void getMonthTasks(Calendar currentdate) {
        int month = currentdate.get(Calendar.MONTH);
        int year = currentdate.get(Calendar.YEAR);
        int lastDayOfMonth = -1;
        //Last day of month
        if(month == Calendar.APRIL || month == Calendar.JUNE || month == Calendar.SEPTEMBER || month == Calendar.NOVEMBER)
            lastDayOfMonth = 31;
        else if( month == Calendar.FEBRUARY ){
            if((year%4 == 0 && year%100!=0) || (year%4==0 && year%100==0 && year%400==0))
                lastDayOfMonth = 29;
            else
                lastDayOfMonth = 28;
        }
        else
            lastDayOfMonth = 30;
        Log.d(TAG, "onViewCreated: month = "+month+" lastDayOfMonth ="+lastDayOfMonth);

        //elenco
        Calendar endList = Calendar.getInstance();
        endList.set(Calendar.YEAR,year);
        endList.set(Calendar.MONTH,month);
        endList.set(Calendar.DAY_OF_MONTH,lastDayOfMonth);
        endList.set(Calendar.HOUR_OF_DAY,23);
        endList.set(Calendar.MINUTE,59);

        Calendar startList = Calendar.getInstance();
        startList.set(Calendar.HOUR_OF_DAY,0);
        startList.set(Calendar.MINUTE,0);

        //Weekview
        Calendar endWeekView = Calendar.getInstance();
        endWeekView.set(Calendar.YEAR,year);
        endWeekView.set(Calendar.MONTH,month);
        endWeekView.set(Calendar.DAY_OF_MONTH,lastDayOfMonth);
        endWeekView.set(Calendar.HOUR_OF_DAY,23);
        endWeekView.set(Calendar.MINUTE,59);

        Calendar startWeekView = Calendar.getInstance();
        startWeekView.set(Calendar.YEAR,year);
        startWeekView.set(Calendar.MONTH,month);
        startWeekView.set(Calendar.DAY_OF_MONTH,1);
        startWeekView.set(Calendar.HOUR_OF_DAY,0);
        startWeekView.set(Calendar.MINUTE,0);
        mRepository.getUpcommingTasks(startList.getTimeInMillis(), endList.getTimeInMillis(), new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                for(Task t : tasks)
                    Log.d(TAG, "GET UPCOMING TASKS success : task = "+t);
            }
        });
    }

    @Override
    public void deleteAllTasks() {
        mRepository.deleteAllTasks(new TasksDataSource.DBCallback() {
            @Override
            public void success() {
                Log.d(TAG, "success: DELETE ALL TASKS!!!");
            }

            @Override
            public void failure() {

            }
        });
    }


}
