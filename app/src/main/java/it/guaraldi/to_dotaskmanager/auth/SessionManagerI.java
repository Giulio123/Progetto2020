package it.guaraldi.to_dotaskmanager.auth;

import java.util.concurrent.Executor;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.util.AppExecutors;

public interface SessionManagerI {

    void createUserSession(String username, String authToken, Executor io, Executor main, SessionCallback callback);
    void getUserSession(Executor io, Executor main, SessionCallback callback);
    void deleteUserSession(Executor io, Executor main, SessionCallback callback);
    void updateUserSession(Executor io, Executor main, String username, String authToken, SessionCallback callback);

    interface SessionCallback{
       void success(String result);
       void failure(String errMsg);
    }

}
