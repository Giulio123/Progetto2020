package it.guaraldi.to_dotaskmanager.taskdetails;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface TaskDetailsContract {

    interface View extends IBaseView {

        void updateViewTaskData( String title, String email, int priority, String category, String start, String end,
                                 String description, String color, String status);
    }

    interface Presenter {
        void getTaskById(int taskId);
    }
}
