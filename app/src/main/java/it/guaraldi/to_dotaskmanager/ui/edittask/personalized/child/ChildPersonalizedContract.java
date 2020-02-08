package it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child;

import java.util.Calendar;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface ChildPersonalizedContract {

    interface View extends IBaseView{
        void loadMonthSpinnerData(String [] data);
        void loadLastDay(String date);
        void loadCurrentDay(int dayPosition);
    }

    interface Presenter{
        void updateCurrentDay(String date);
        void calculateMonthOccurrences(String date,String prefix,String [] nameOccurrences);
        void setUpLastDay(String date);
        void updateLastDay(int year, int month, int dayOfMonth);
    }
}
