package it.guaraldi.to_dotaskmanager.taskdetails;

import android.os.Bundle;

import java.util.List;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface TaskDetailsContract {

    interface View extends IBaseView {

        void updateViewTaskData(List<String> taskDetails);
        void showCalendarView();
        void showEditTaskView(Bundle data);
    }

    interface Presenter {

        void getTaskById(int taskId);
        void deleteTaskById(int taskId);
        void modifyTask(int tasId);
    }
}
