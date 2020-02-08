package it.guaraldi.to_dotaskmanager.ui.registration;

import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface RegistrationContract  {
    interface View extends IBaseView {
        void updateViewData(boolean userRegistration, String token, int errorType);
        void showLoginView();
    }
    interface Presenter {
        void doRegistration(String username,String email,String pwd, String [] errors);
        void openLogin();
    }
}