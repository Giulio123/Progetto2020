package it.guaraldi.to_dotaskmanager.taskdetails;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface TaskDetailsContract {

    interface View extends IBaseView {

        void updateViewTaskData( String title,String duration, int priority, String category, String description, String color, String status);
        void showCalendarView();
        void showEditTaskView();
    }

    interface Presenter {

        void getTaskById(int taskId);
        void deleteTaskById(int taskId);
        void modifyTask();
    }
}
