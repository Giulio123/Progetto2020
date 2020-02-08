package it.guaraldi.to_dotaskmanager.ui.edittask;

import java.util.ArrayList;
import java.util.List;

import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedInstanceState;

public interface EditTaskContract {
    interface View extends IBaseView {
        void dataError(String error);
        void updateDate(String newDate, boolean validDate);
        void updateTime(String newTime, boolean validTime);
        void updatePeriod(String period, int position);
        void updateEmail();
        void updateCategory(int position, String category, int typeUpdate);
        void updatePosition();
        void errorSaveTask();
        void showCalendarView(String titleTask, String duration, int id, long startDate, int priority);
        void showPersonalizedView(EditInstanceState state);
        void loadAllCategories(List<String> categories);
        void loadCategory(String category);
        void updateViewTaskData( String title, String email, int priority, String category, String start, String end,
                                 String description, String color);
    }
    interface Presenter {
        void saveNewTask(String title, String email, boolean allDay, int priority, String category, String dateStart,
                         String timeStart, String dateEnd,String timeEnd,String reply,boolean isReply, String description, String color,
                         PersonalizedInstanceState personalized, ChildPersonalizedInstanceState childPersonalizedState);
        void onChangeDatePicker(int year, int month, int dayOfMonth, String [] checkDate, boolean isStartDate);
        void onChangeTimePicker(int hour, int minute, String [] checkDate, boolean isStartTime);
        void createPeriodicTask(String period, int position);
        void openPersonalizedPeriodicTask(EditInstanceState editState);
        void fetchAllCategories();
        void getSelectedCategory(int position, String category);
        void addCategory(String category);
        void deleteCategory(int position, String category);
        void createRepeatedPeriodText(int repetition, String periodName, boolean [] daysSelected,
                                                String monthNameSelected, int monthPosSelected, boolean isAfterBtn,
                                                boolean isTheDayBtn, String [] periodType, String generalPeriod,
                                      int occurences, String endDayTv);
        void createPeriodicTask();
        void updateCurrentTaskId();
        void getTaskById(int taskId);
    }
}