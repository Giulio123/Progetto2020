package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.os.Bundle;

import org.threeten.bp.LocalDate;

import java.util.List;

import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;


public interface CalendarContract {
    interface View extends IBaseView {
        void showCalendar();
        void showTimeTable();
        void showTasksOfMonth();
        void showTaskDetails(Bundle taskData);
        void showEditTaskView(Bundle taskData);
        void reloadActivity();

        void updateData(List<Task> tasks);
        void updateSession(String displayName, String email, int result);
    }

    interface Presenter {
        void addNewTask();
        void checkSession();
        void signOut();

        void loadTasksOfMonth(LocalDate date);
        void loadTasksForNewVisibleDays();
        void openTaskDetails(Bundle taskData);
        void openEditTask(Bundle taskData);

        void getSizeTableTasks();
        void getLastId();
        void printAllTasks();

        void updateMonthTask(LocalDate currentdate);
        void deleteAllTasks();

        List<Task> getTasksOfDay(LocalDate date);
    }

}
