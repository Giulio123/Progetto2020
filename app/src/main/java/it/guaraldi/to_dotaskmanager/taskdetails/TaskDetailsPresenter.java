package it.guaraldi.to_dotaskmanager.taskdetails;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;
import it.guaraldi.to_dotaskmanager.utils.DateUtils;

public class TaskDetailsPresenter extends BasePresenter<TaskDetailsContract.View> implements TaskDetailsContract.Presenter {

    private final TasksRepository mRepository;
    private static final String TAG = "TaskDetailsPresenter";

    @Inject
    public TaskDetailsPresenter(TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void getTaskById(int taskId) {
        Log.d(TAG, "getTaskById: ");
        mRepository.getTaskById(taskId, new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                Log.d(TAG, "success: ");
                if(tasks.size() == 1) {
                    Log.d(TAG, "success: 1 if");
                    String duration = null;
                    Task task = tasks.get(0);
                    Calendar c= Calendar.getInstance();
                    Calendar ts = Calendar.getInstance();
                    ts.setTimeInMillis(Long.parseLong(task.getStart()));
                    c.setTimeInMillis(System.currentTimeMillis());
                    if(c.get(Calendar.DAY_OF_MONTH)==ts.get(Calendar.DAY_OF_MONTH)
                            && c.get(Calendar.MONTH)==ts.get(Calendar.MONTH)
                            && c.get(Calendar.YEAR)==ts.get(Calendar.YEAR)){
                        Log.d(TAG, "success: 2 if");
                        c.setTimeInMillis(Long.parseLong(task.getEnd()));
                        duration = "Today: "+ts.get(Calendar.HOUR_OF_DAY)+":"+ts.get(Calendar.MINUTE)+"-"
                                +c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
                    }
                    else {
                        Log.d(TAG, "success: else");
                        duration = DateUtils.longtoStringDayMonth(Long.parseLong(task.getStart())) + " * "
                                + DateUtils.longToStringTimeDate(Long.parseLong(task.getStart())) + "-" +
                                DateUtils.longToStringTimeDate(Long.parseLong(task.getEnd()));
                    }
                    List<String> taskDetails = new ArrayList<>();
                    taskDetails.add(task.getTitle());
                    taskDetails.add(duration);
                    taskDetails.add("Priority: "+String.valueOf(task.getPriority()));
                    taskDetails.add("Category: "+task.getCategory());
                    taskDetails.add("Status: "+task.getStatus());
                    taskDetails.add("Description: "+task.getDescription());
                    mView.updateViewTaskData(taskDetails);
                }
            }
        });
    }

    @Override
    public void deleteTaskById(int taskId) {
        Log.d(TAG, "deleteTaskById: ");
        mRepository.deleteTask(taskId, new TasksDataSource.DBCallback() {
            @Override
            public void success() {
                Log.d(TAG, "success: ");
                mView.showCalendarView();
            }

            @Override
            public void failure() {
                Log.d(TAG, "failure: ");
            }
        });
    }

    @Override
    public void modifyTask(int taskId) {
        Bundle data = new Bundle();
        data.putInt(ActivityUtils.ID_TASK,taskId);
        mView.showEditTaskView(data);
    }
}
