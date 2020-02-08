package it.guaraldi.to_dotaskmanager.data.local;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;



import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.local.dao.TaskDao;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.util.AppExecutors;

public class LocalDataSource implements TasksDataSource {

    private TaskDao mTaskDao;
    private AppExecutors mAppExecutors;
      @Inject
    public LocalDataSource(AppExecutors appExecutors,
                                 TaskDao taskDao){
        mAppExecutors = appExecutors;
        mTaskDao = taskDao;
    }

    @Override
    public void addCategory(String category, LoadCategory loadCategory) {
        // DONT WORK
    }

    @Override
    public void loadCategories(LoadCategories loadCategories) {
        // DONT WORK
    }

    @Override
    public void deleteCategory(String category, LoadCategory loadCategory) {
        // DONT WORK
    }

    @Override
    public void authentication(String email, String pwd, FirebaseCallback callback) {

    }

    @Override
    public void registration(String username, String email, String password, FirebaseCallback callback) {

    }

    @Override
    public void reloadUser(FirebaseCallback callback) {

    }

    @Override
    public void deleteCurrentUser(FirebaseCallback callBack) {

    }


    @Override
    public void getCurrentUser(LoadSessionCallback callback) {

    }

    @Override
    public void signOut(SignOutCallback callback) {

    }

    @Override
    public void createTask(Task newTask, DBCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.insertTask(newTask);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success();
                    }
                });
            }
        });
    }

    @Override
    public void changeTaskStatus(String status, int taskId, DBCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.changeTaskStatus(status,taskId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success();
                    }
                });
            }
        });

    }

    @Override
    public void getLastTaskId(DBCallbackId callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int lastId = mTaskDao.getLastTaskId();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(lastId);
                    }
                });
            }
        });
    }

    @Override
    public void getSizeTableTaks(DBCallbackSize callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int sizeTableTaks = mTaskDao.getSizeTableTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(sizeTableTaks);
                    }
                });
            }
        });
    }

    @Override
    public void getAllTasks(DBCallBackTasks callBackTaks) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Task> tasks = mTaskDao.getTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBackTaks.success(tasks);
                    }
                });
            }
        });
    }

    @Override
    public void getUpcommingTasks(long startDate, long endDate, DBCallBackTasks callBackTasks) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Task> tasks = mTaskDao.getUpcomingTasks(startDate,endDate);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBackTasks.success(tasks);
                    }
                });
            }
        });
    }

    @Override
    public void deleteTask(int taskId, DBCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteTask(taskId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success();
                    }
                });
            }
        });
    }

    @Override
    public void deleteAllTasks(DBCallback callback) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteAllTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success();
                    }
                });
            }
        });
    }

    @Override
    public void getTaskById(int taskId, DBCallBackTasks callBackTasks) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Task> tasks = new ArrayList<>();
                tasks.add(mTaskDao.getTaskById(taskId));
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBackTasks.success(tasks);
                    }
                });
            }
        });
    }


    @Override
    public void reauthentication(String email, String password, FirebaseCallback callback) {

    }

    @Override
    public void getAuthToken(LoadStringCallback stringcallback) {

    }

}
