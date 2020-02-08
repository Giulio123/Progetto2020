package it.guaraldi.to_dotaskmanager.notification;

import android.util.Log;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;

public class NotificationReceiverPresenter extends BasePresenter<NotificationReceiverContract.View> implements NotificationReceiverContract.Presenter {

    private TasksRepository mRepository;
    private static final String TAG = "NotificationReceiverPre";
    @Inject
    public NotificationReceiverPresenter(TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void changeTaskStatus(String status, int taskId) {
        status = status.replaceAll("it.guaraldi.to_dotaskmanager.", "");
        mRepository.changeTaskStatus(status, taskId, new TasksDataSource.DBCallback() {
            @Override
            public void success() {

//
//                if(status == "it.guaraldi.to_dotaskmanager.COMPLETE"){
//
//                }
//                if(status == "it.guaraldi.to_dotaskmanager.ONGOING"){
//
//                }
            }

            @Override
            public void failure() {

            }
        });
    }
}
