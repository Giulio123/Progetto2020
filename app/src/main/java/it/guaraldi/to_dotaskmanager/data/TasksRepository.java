package it.guaraldi.to_dotaskmanager.data;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


import it.guaraldi.to_dotaskmanager.data.local.entities.Task;


public class TasksRepository implements TasksDataSource {
    private static final String TAG = "TasksRepository";
    private static final String CATEGORY_SHARED = "CATEGORY";
    private static final String TOTAL_CATEGORY = "TOTAL_CATEGORY";

    private SharedPreferences mPreferences;

    private SharedPreferences.Editor mEditor;

    private final TasksDataSource mLocalDataSource;

    private final TasksDataSource mRemoteDataSource;

    private int mSizeTableTasks = -1;

    private int mLastId = -1;


    Map<String, Task> mCachedTasks;

    boolean mCacheIsDirty = false;

    @Inject
    public TasksRepository(TasksDataSource localDataSource,
                            TasksDataSource remoteDataSource,SharedPreferences preferences){
        mPreferences = preferences;
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mEditor = mPreferences.edit();
    }


    private boolean putCategory(String category){
        mEditor.putString(Integer.toString(category.hashCode()),category);
        return mEditor.commit();
    }
    private String getCategory(String category){
        return mPreferences.getString(Integer.toString(category.hashCode()), null);
    }
    private List<String> getCategories() {
        readPreference();
        Map<String,?> map = mPreferences.getAll();
        List<String> res = new ArrayList<>();
        for (Map.Entry<String,?> entry : map.entrySet())
            if(entry.getValue()!=null)
                res.add((String) entry.getValue());
        return res;
    }
    private void readPreference(){
        Map<String,?> map = mPreferences.getAll();
        for (Map.Entry<String,?> entry : map.entrySet()) {
            Log.d(TAG, "readPreference: "+entry.getKey() + "/" + entry.getValue());
        }
    }

    @Override
    public void addCategory(String category, LoadCategory loadCategory) {
        if(putCategory(category))
            loadCategory.success(getCategory(category));
        else
            loadCategory.failure("addCategory() failure");
    }

    @Override
    public void loadCategories(LoadCategories loadCategories) {
        List<String> categories = getCategories();
        if(categories!=null)
            loadCategories.success(categories);
        else
            loadCategories.failure("loadCategories() failure");
    }

    @Override
    public void deleteCategory(String category, LoadCategory loadCategory) {
        String res = mPreferences.getString(Integer.toString(category.hashCode()),null);
        mEditor.remove(Integer.toString(category.hashCode()));
        if(mEditor.commit())
            loadCategory.success(res);
        else
            loadCategory.failure("deleteCategory().failure()");
    }

    @Override
    public void authentication(String email, String pwd, LoadSessionCallback callback) {
        Log.d(TAG, "authentication: ");
        mRemoteDataSource.authentication(email, pwd, new LoadSessionCallback() {
            @Override
            public void success(User user) {
                callback.success(user);
            }

            @Override
            public void failure(Exception e) {
                     callback.failure(e);
            }
        });
    }



    @Override
    public void registration(String username, String email, String password, FirebaseCallback callback) {
        mRemoteDataSource.registration(username, email, password, new FirebaseCallback() {
            @Override
            public void success(com.google.android.gms.tasks.Task<?> task) {
                callback.success(task);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });
    }

    @Override
    public void reloadUser(FirebaseCallback callback) {
        mRemoteDataSource.reloadUser(new FirebaseCallback() {
            @Override
            public void success(com.google.android.gms.tasks.Task<?> task) {
                callback.success(task);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });
    }

    @Override
    public void deleteCurrentUser(FirebaseCallback callback) {
        mRemoteDataSource.deleteCurrentUser(new FirebaseCallback() {
            @Override
            public void success(com.google.android.gms.tasks.Task<?> task) {
                callback.success(task);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });
    }



    @Override
    public void getCurrentUser(LoadSessionCallback callback) {
        mRemoteDataSource.getCurrentUser(new LoadSessionCallback() {
            @Override
            public void success(User user) {
                callback.success(user);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });

    }

    @Override
    public void signOut(SignOutCallback callback) {
        mRemoteDataSource.signOut(new SignOutCallback() {
            @Override
            public void success() {
                callback.success();
            }
        });
    }

    @Override
    public void createTask(Task newTask, DBCallback callback) {
        mLocalDataSource.createTask(newTask, new DBCallback() {
            @Override
            public void success() {
                mLastId= Integer.parseInt(newTask.getId());
                callback.success();
            }

            @Override
            public void failure() {
                callback.failure();
            }
        });
    }

    @Override
    public void changeTaskStatus(String status, int taskId, DBCallback callback) {
        mLocalDataSource.changeTaskStatus(status, taskId, new DBCallback() {
            @Override
            public void success() {
                callback.success();
            }

            @Override
            public void failure() {
                callback.failure();
            }
        });
    }

    @Override
    public void getLastTaskId(DBCallbackId callback) {
        mLocalDataSource.getLastTaskId(new DBCallbackId() {
            @Override
            public void success(int lastId) {
                callback.success(lastId);
            }
        });
    }

    @Override
    public void getSizeTableTaks(DBCallbackSize callback) {
        mLocalDataSource.getSizeTableTaks(new DBCallbackSize() {
            @Override
            public void success(int sizeListTask) {
                mSizeTableTasks = sizeListTask;
                callback.success(sizeListTask);
            }
        });
    }

    @Override
    public void getAllTasks(DBCallBackTasks callBackTaks) {
        mLocalDataSource.getAllTasks(new DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                callBackTaks.success(tasks);
            }
        });
    }

    @Override
    public void getUpcommingTasks(long startDate, long endDate, DBCallBackTasks callBackTasks) {
        mLocalDataSource.getUpcommingTasks(startDate, endDate, new DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                callBackTasks.success(tasks);
            }
        });
    }

    @Override
    public void deleteTask(int taskId, DBCallback callback) {
        mLocalDataSource.deleteTask(taskId, new DBCallback() {
            @Override
            public void success() {
                callback.success();
            }

            @Override
            public void failure() {
                callback.failure();
            }
        });
    }

    @Override
    public void deleteAllTasks(DBCallback callback) {
        mLocalDataSource.deleteAllTasks(new DBCallback() {
            @Override
            public void success() {
                callback.success();
            }

            @Override
            public void failure() {
                callback.failure();
            }
        });
    }

    @Override
    public void getTaskById(int taskId, DBCallBackTasks callBackTasks) {
        mLocalDataSource.getTaskById(taskId, new DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                callBackTasks.success(tasks);
            }
        });

    }


    @Override
    public void reauthentication(String email, String password, FirebaseCallback callback) {
        mRemoteDataSource.reauthentication(email, password, new FirebaseCallback() {
            @Override
            public void success(com.google.android.gms.tasks.Task<?> task) {
                callback.success(task);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });
    }

    @Override
    public void getAuthToken(LoadStringCallback callback) {
        mRemoteDataSource.getAuthToken(new LoadStringCallback() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(Exception e) {
                callback.failure(e);
            }
        });
    }

    public int getSizeTableTasks(){
       return mSizeTableTasks;
    }

    public int getLastId(){
        return mLastId;
    }
}
