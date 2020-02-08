package it.guaraldi.to_dotaskmanager.taskdetails;

import android.nfc.Tag;

import java.util.List;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;

public class TaskDetailsPresenter extends BasePresenter<TaskDetailsContract.View> implements TaskDetailsContract.Presenter {

    private final TasksRepository mRepository;
    private static final String TAG = "TaskDetailsPresenter";

    @Inject
    public TaskDetailsPresenter(TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void getTaskById(int taskId) {
        mRepository.getTaskById(taskId, new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                if(tasks.size() == 1) {
                    Task task = tasks.get(0);
                    mView.updateViewTaskData(task.getTitle(),task.getEmail(), task.getPriority(),task.getCategory(),task.getStart(),task.getEnd(),
                            task.getDescription(),task.getColor(),task.getStatus());
                }
            }
        });
    }
}
