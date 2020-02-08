package it.guaraldi.to_dotaskmanager.ui.login;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface LoginContract {
    interface View extends IBaseView {
        void errorData(int [] credentialStatus, String [] errorMsgs);
        void showCalendarView();
        void showRegistrationView();
    }
    interface Presenter {
        void doLogin(String email,String password, String [] errors);
        void openRegistration();
    }
}