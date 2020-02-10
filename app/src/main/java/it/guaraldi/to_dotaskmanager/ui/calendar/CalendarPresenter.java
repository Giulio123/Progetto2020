package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.os.Bundle;
import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.User;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

public class CalendarPresenter extends BasePresenter<CalendarContract.View> implements CalendarContract.Presenter {
    private final TasksRepository mRepository;
    private static final String TAG = "CalendarPresenter";

    private List<Task> tasks = null;

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
    public void loadTasksOfMonth(LocalDate date) {
        mRepository.getTaskByMonth(date, new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                mView.updateData(tasks);
            }
        });
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
                Bundle data = new Bundle();
                data.putInt(ActivityUtils.ID_TASK,lastId);
                mView.showTaskDetails(data);
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
    public void updateMonthTask(LocalDate currentdate) {
//        mRepository.getTaskByMonth(currentdate, new TasksDataSource.DBCallBackTasks() {
//            @Override
//            public void success(List<Task> tasksMonth) {
//                tasks = tasksMonth;
//            }
//        });
        mRepository.getAllTasks(new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasksMonth) {
                tasks = tasksMonth;
                mView.updateData(tasks);
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

    @Override
    public List<Task> getTasksOfDay(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        Long startDay = date.atStartOfDay(zoneId).toEpochSecond();
        Long endDay = date.plusDays(1).atStartOfDay(zoneId).toEpochSecond();
        List<Task> tasksDay = new ArrayList<>();
        if (tasks != null) {
            for (Task t : tasks) {
                Long startDate = Long.parseLong(t.getStart()) / 1000;
                if (startDate > startDay && startDate < endDay) {
                    tasksDay.add(t);
                }
            }
        }
        return tasksDay;
    }

}
