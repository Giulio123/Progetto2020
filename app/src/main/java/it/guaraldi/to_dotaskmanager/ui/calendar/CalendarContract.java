package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.concurrent.Executor;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;


public interface CalendarContract {
    interface View extends IBaseView {
        void showCalendar();
        void showTimeTable();
        void showTasksOfMonth();
        void showTaskDetails(Bundle taskData);
        void showEditTaskView(Bundle taskData);
        void reloadActivity();
        void updateData();
        void updateSession(String displayName, String email, int result);
    }

    interface Presenter {
        void addNewTask();
        void checkSession();
        void signOut();
        void loadTasksOfMonth();
        void loadTasksForNewVisibleDays();
        void openTaskDetails(Bundle taskData);
        void openEditTask(Bundle taskData);

        void getSizeTableTasks();
        void getLastId();
        void printAllTasks();
        void getMonthTasks(Calendar currentdate);
        void deleteAllTasks();
    }

}
