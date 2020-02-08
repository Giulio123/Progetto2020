package it.guaraldi.to_dotaskmanager.ui.login;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.CPWD;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.EMAIL;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.EMAIL_L;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.INVALID_FIELD;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.NOT_MATCH;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.PWD;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.PWD_L;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.SERVER_ERR;
import static it.guaraldi.to_dotaskmanager.ui.login.LoginFragment.USERNAME;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private TasksRepository mRepository;
    private static final String TAG = "LoginPresenter";
    @Inject public LoginPresenter (TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void doLogin(String email, String password, String[] errors) {
        int [] credentialStatus = validateUserData(new String[]{email,password});
        if(!allCredentialsAreValid(credentialStatus))
            mView.errorData(credentialStatus,null);
        else{
            mRepository.authentication(email, password, new TasksDataSource.FirebaseCallback() {
            @Override
            public void success(Task<?> task) {
                String authToken = ((GetTokenResult)task.getResult()).getToken();
            }

            @Override
            public void failure(Exception e) {
                mView.errorData(new int[]{SERVER_ERR,SERVER_ERR}, new String[]{e.getMessage(),e.getMessage()});
                Log.d(TAG, "failure: "+e.getMessage()+"code:"+e.getLocalizedMessage());
            }
            });
        }
    }


    @Override
    public void openRegistration() {
        mView.showRegistrationView();
    }

    private int [] validateUserData(String [] userData){
        int [] result = new int[userData.length];
        Arrays.fill(result,0);
        if(userData.length > 2){
            if(!usernameValidation(userData[USERNAME]))
                result[USERNAME] = INVALID_FIELD;
            if(!emailValidation(userData[EMAIL_L]))
                result[EMAIL] = INVALID_FIELD;
            if(!pwdValidation(userData[PWD_L],userData[EMAIL_L])){
                result[PWD] = INVALID_FIELD;
            }

            if(userData[PWD].equals(userData[CPWD]) && !pwdValidation(userData[CPWD],userData[EMAIL]))
                result[CPWD] = INVALID_FIELD;
            if(!userData[CPWD].equals(userData[PWD]))
                result[CPWD] = NOT_MATCH;
        }
        else {
            if(!emailValidation(userData[EMAIL_L]))
                result[EMAIL_L] = INVALID_FIELD;
            if(!pwdValidation(userData[PWD_L],userData[EMAIL_L])){
                result[PWD_L] = INVALID_FIELD;
            }
        }
        return result;
    }
    private void handleServerError(String error,int [] credentialStatus, String [] errorMsgs){
        if(error.equals(errorMsgs));
    }
    private boolean allCredentialsAreValid(int [] credentialStatus ){
        for(int i : credentialStatus)
            if(i != 0)
                return false;
        return true;
    }
    private int checkLoginData(String email,String password){
        if(!emailValidation(email) && !pwdValidation(password,email))
            return ActivityUtils.INVALID_EMAIL_PASSWORD;
        if(!emailValidation(email))
            return ActivityUtils.INVALID_EMAIL;
        if(!pwdValidation(password,email))
            return ActivityUtils.INVALID_PASSWORD;
        return ActivityUtils.ALL_FIELDS_ARE_VALID;
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

    private void readTokenTask(Task<?> task){
        String token = ((GetTokenResult)task.getResult()).getToken();
        String signInProvider =((GetTokenResult)task.getResult()).getSignInProvider();
        long authTimestamp = ((GetTokenResult)task.getResult()).getAuthTimestamp();
        long expirationTimestamp = ((GetTokenResult)task.getResult()).getExpirationTimestamp();
        long issuedAtTimestamp = ((GetTokenResult)task.getResult()).getIssuedAtTimestamp();
        Map<String,Object> claims = ((GetTokenResult)task.getResult()).getClaims();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(authTimestamp);
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(expirationTimestamp);
        Calendar e = Calendar.getInstance();
        e.setTimeInMillis(issuedAtTimestamp);
        Log.d(TAG, "SUCCESS:\ntoken:"+token+"\nauthTimestamp: "+c.getTime()+
                "\nexpirationTimestamp: "+d.getTime()+
                "\nissuedAtTimestamp: "+e.getTime()+
                "\nsingInProvider:"+signInProvider);
    }
}
