package it.guaraldi.to_dotaskmanager.data.source.local;

import it.guaraldi.to_dotaskmanager.data.source.TasksDataSource;
import it.guaraldi.to_dotaskmanager.util.AppExecutors;

/**
 * Created by sugfdo on 10/06/19.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TaskDao mTaskDao;

    private AppExecutors mAppExecutors;

    //Prevent direct instantiation
    private TasksLocalDataSource(AppExecutors appExecutors,
                                 TaskDao taskDao){
        mAppExecutors = appExecutors;
        mTaskDao = taskDao;
    }

    public static TasksLocalDataSource getInstance(AppExecutors appExecutors, TaskDao taskDao){

        if(INSTANCE == null)
            synchronized (TasksLocalDataSource.class) {
                if(INSTANCE == null)
                    INSTANCE = new TasksLocalDataSource(appExecutors, taskDao);

            }
        return INSTANCE;
    }

    static void clearInstance() {
        INSTANCE = null;
    }
}
