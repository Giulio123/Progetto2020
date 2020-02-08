package it.guaraldi.to_dotaskmanager.ui.registration;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.ALL_FIELDS_ARE_VALID;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.USERNAME_TAKEN;

public class RegistrationPresenter extends BasePresenter<RegistrationContract.View> implements RegistrationContract.Presenter {
    private TasksRepository mRepository;
    private static final String TAG = "RegistrationPresenter";
    @Inject
    public RegistrationPresenter (TasksRepository repository){
      mRepository = repository;
    }


    @Override
    public void doRegistration(String username, String email, String pwd, String [] errors) {
        int validation = checkSingUpData(username,email,pwd);
        if(validation != ActivityUtils.ALL_FIELDS_ARE_VALID)
            mView.updateViewData(false,null,validation);
        else {
           mRepository.registration(username, email, pwd, new TasksDataSource.FirebaseCallback() {
               @Override
               public void success(Task<?> task) {
                   String token = ((GetTokenResult)task.getResult()).getToken();
//                   String signInProvider =((GetTokenResult)task.getResult()).getSignInProvider();
//                   long authTimestamp = ((GetTokenResult)task.getResult()).getAuthTimestamp();
//                   long expirationTimestamp = ((GetTokenResult)task.getResult()).getExpirationTimestamp();
//                   long issuedAtTimestamp = ((GetTokenResult)task.getResult()).getIssuedAtTimestamp();
//                   Map<String,Object> claims = ((GetTokenResult)task.getResult()).getClaims();
//                   Log.d(TAG, "SUCCESS:\ntoken:"+token+"\nauthTimestamp: "+ DateUtils.longToStringCompleteInformationDate(authTimestamp)+
//                           "\nexpirationTimestamp: "+DateUtils.longToStringCompleteInformationDate(expirationTimestamp)+
//                           "\nissuedAtTimestamp: "+DateUtils.longToStringCompleteInformationDate(issuedAtTimestamp)+
//                           "\nsingInProvider:"+signInProvider);
                   mView.updateViewData(true,token,ALL_FIELDS_ARE_VALID);
                   mView.showLoginView();
               }

               @Override
               public void failure(Exception e) {
                   for(String error: errors)
                       if(error.equals(e.getMessage())) {
                           mView.updateViewData(false,null,USERNAME_TAKEN);
                           break;
                       }
                   Log.d(TAG, "failure: msg:" + e.getMessage());
               }
           });
        }

    }

    @Override
    public void openLogin() {
        mView.showLoginView();
    }


    private int checkSingUpData(String username, String email, String pwd){
            if(usernameValidation(username) && emailValidation(email) && pwdValidation(pwd,email))
                return ActivityUtils.ALL_FIELDS_ARE_VALID;
            if(!usernameValidation(username) && !emailValidation(email) && !pwdValidation(pwd,email))
              return ActivityUtils.ALL_FIELDS_ARE_INVALID;
            if(!usernameValidation(username) && !emailValidation(email))
                return ActivityUtils.INVALID_USERNAME_EMAIL;
            if(!usernameValidation(username) && !pwdValidation(pwd,email))
                return ActivityUtils.INVALID_USERNAME_PASSWORD;
            if(!emailValidation(email) && !pwdValidation(pwd,email))
                return ActivityUtils.INVALID_EMAIL_PASSWORD;
            if(!usernameValidation(username))
                return ActivityUtils.INVALID_USERNAME;
            if(!emailValidation(email))
                return ActivityUtils.INVALID_EMAIL;
            if(!pwdValidation(pwd,email))
                return ActivityUtils.INVALID_PASSWORD;
        return -1;
    }

        private boolean usernameValidation(String username){
            String regex = "^(?=.*\\w)(?=.*[`~!@#$%^&*(|'\";:,<.>/)_=+{}]*).{3,20}$";
            return username.matches(regex);
        }
    private boolean pwdValidation(String pwd, String email){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[`~!@#$%^&*(|'\";:,<.>/)_=+{}])(?!.*"+email+")(?!.*(.)\\1{2,}).{8,20}$";
        return pwd.matches(regex);
    }

    private boolean emailValidation(String email){
        String regex = "^[\\w&&[^_]]+(?:((\\.)|(\\-)|(\\_)|(\\&))[\\w&&[^_]]+)*@[\\w&&[^_]]+(?:((\\.)|(\\-)|(\\_)|(\\&))[\\w&&[^_]]+)*(?:(\\.)[\\w&&[^_\\d]]{2,6}){1}$";
        return email.length()>5 && email.matches(regex);
    }
}