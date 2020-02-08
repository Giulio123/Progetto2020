package it.guaraldi.to_dotaskmanager.data.source.remote;


import it.guaraldi.to_dotaskmanager.data.source.TasksDataSource;

/**
 * Created by sugfdo on 10/06/19.
 */

public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource INSTANCE;

    //prevent direct instantiation
    private TasksRemoteDataSource(){}

    public static TasksRemoteDataSource getInstance(){

        if(INSTANCE == null)
            INSTANCE = new TasksRemoteDataSource();

        return INSTANCE;
    }

    static void clearInstance() {
        INSTANCE = null;
    }

}
