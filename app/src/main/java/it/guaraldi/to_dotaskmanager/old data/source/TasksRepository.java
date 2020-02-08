package it.guaraldi.to_dotaskmanager.data.source;


import java.util.Map;

import it.guaraldi.to_dotaskmanager.data.Task;

/**
 * Created by sugfdo on 10/06/19.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;

    boolean mCacheIsDirty = false;


    private TasksRepository (TasksDataSource tasksRemoteDataSource,
                             TasksDataSource tasksLocalDataSource){
        mTasksLocalDataSource = tasksLocalDataSource;
        mTasksRemoteDataSource = tasksRemoteDataSource;
    }

    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                             TasksDataSource tasksLocalDataSource){
        if(INSTANCE == null)
            INSTANCE = new TasksRepository(tasksRemoteDataSource,tasksLocalDataSource);

        return INSTANCE;
    }

    static void clearInstance() {
        INSTANCE = null;
    }
}
