package it.guaraldi.to_dotaskmanager.ui.graphic;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;

public class GraphicPresenter extends BasePresenter<GraphicContract.View> implements GraphicContract.Presenter {
    private static final String TAG = "GraphicPresenter";
    TasksRepository mRepository;
    @Inject
    public GraphicPresenter(TasksRepository repository){
        mRepository = repository;
    }


    @Override
    public void getAllTasksByCategory(String category) {
        Log.d(TAG, "getAllTasksByCategory: ");
        mRepository.getAllTaskByCategory(category,new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                Log.d(TAG, "success: ");
                int []totalTask =  new int[4];
                int []taskPending= new int[4];
                int [] taskComplete=  new int[4];
                Arrays.fill(totalTask,0);
                Arrays.fill(totalTask,0);
                Arrays.fill(totalTask,0);
                Arrays.fill(totalTask,0);

                for(Task t: tasks) {
                    int index = t.getPriority()-1;
                    if(t.getStatus().equals("PENDING"))
                        taskPending[index]+=1;
                    else
                        taskComplete[index]+=1;
                    totalTask[index]+= 1;
                    Log.d(TAG, "success: t=" + t.toString());
                    Log.d(TAG, "success: Total["+t.getPriority()+"] ="+taskPending[t.getPriority()-1]+
                                    " Pending["+t.getPriority()+"] ="+taskPending[t.getPriority()-1]+
                                    "Complete["+t.getPriority()+"] ="+taskComplete[t.getPriority()-1]);
                }
                mView.getTasksForGraphic(tasks.get(0).getCategory(),totalTask,taskPending,taskComplete);
            }
        });

    }
}
