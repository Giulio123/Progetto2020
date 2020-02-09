package it.guaraldi.to_dotaskmanager.data;
import com.google.android.gms.tasks.Task;
import java.util.List;



/**
 * Created by sugfdo on 09/06/19.
 */

public interface TasksDataSource {
    void addCategory(String category,LoadCategory loadCategory);
    void loadCategories(LoadCategories loadCategories);
    void deleteCategory(String category,LoadCategory loadCategory);

    void authentication(String email, String pwd, LoadSessionCallback callback);
    void registration(String username, String email, String password, FirebaseCallback callback);
    void reloadUser(FirebaseCallback callback);
    void deleteCurrentUser(FirebaseCallback callBack);
    
    void getCurrentUser(LoadSessionCallback callback);
    void signOut(SignOutCallback callback);

    //DB DATA
    void createTask(it.guaraldi.to_dotaskmanager.data.local.entities.Task newTask, DBCallback callback);
    void changeTaskStatus(String status,int taskId,DBCallback callback);
    void getLastTaskId(DBCallbackId callback);
    void getSizeTableTaks(DBCallbackSize callback);
    void getAllTasks(DBCallBackTasks callBackTaks);
    void getUpcommingTasks(long startDate,long endDate,DBCallBackTasks callBackTasks);
    void deleteTask(int taskId, DBCallback callback);
    void deleteAllTasks(DBCallback callback);
    void getTaskById(int taskId, DBCallBackTasks callBackTasks);
    void getAllTaskByCategory(String category,DBCallBackTasks callBackTasks);
    //TODO RISTRUTTARE STA MERDA
    void reauthentication(String email, String password, FirebaseCallback callback);
    void getAuthToken(LoadStringCallback stringcallback);

    interface DBCallBackTasks{
        void success(List<it.guaraldi.to_dotaskmanager.data.local.entities.Task> tasks);
    }
    interface DBCallbackSize{
        void success(int sizeListTask);
    }
    interface DBCallbackId{
        void success(int lastId);
    }
    interface DBCallback{
        void success();
        void failure();
    }
    interface SignOutCallback{
        void success();
    }

    interface LoadSessionCallback{
        void success(User user);
        void failure(Exception e);
    }

    interface LoadStringCallback{
        void success(String result);
        void failure(Exception e);
    }

    interface LoadCategory{
        void success(String category);
        void failure(String error);
    }

    interface LoadCategories{
        void success(List<String> categories);
        void failure(String error);
    }

    interface FirebaseCallback{
        void success(Task<?> task);
        void failure(Exception e);
    }

}
