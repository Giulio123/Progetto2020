package it.guaraldi.to_dotaskmanager.notification;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface NotificationISContract {

    interface View extends IBaseView{

    }

    interface Presenter{
        void changeTaskStatus(String status, int taskId);
    }
}
