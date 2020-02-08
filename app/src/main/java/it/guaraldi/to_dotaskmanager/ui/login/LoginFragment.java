package it.guaraldi.to_dotaskmanager.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;
import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;

import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.ALL_FIELDS_ARE_VALID;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.INVALID_EMAIL;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.INVALID_EMAIL_PASSWORD;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.INVALID_PASSWORD;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.USER_DISABLED;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.WRONG_EMAIL_OR_PASSWORD;

public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {
    public static final int USERNAME = 0;
    public static final int EMAIL = 1;
    public static final int PWD = 2;
    public static final int CPWD = 3;

    public static final int INVALID_FIELD = 1;
    public static final int SERVER_ERR = 2;
    public static final int NOT_MATCH = 3;
    public static final int EMAIL_L = USERNAME;
    public static final int PWD_L = EMAIL;
    @Inject LoginPresenter mPresenter;
    private static final String TAG = "LoginFragment";
    private EditText emailInput;
    private EditText pwdInput;
    private Button loginBtn;
    private Button singUpBtn;
    private TextView emailError;
    private TextView pwdError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        return inflater.inflate(R.layout.login_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initViews();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

//
//    @Override
//    public void errorData(int errorType) {
//        showCredentialsError(errorType);
//    }

    @Override
    public void errorData(int[] credentialStatus, String[] errorMsgs) {
    }

    @Override
    public void showCalendarView(String token) {
        showCredentialsError(ALL_FIELDS_ARE_VALID);
        Snackbar.make(getView(),getString(R.string.login_successful),Snackbar.LENGTH_SHORT).show();
        getActivity().finish();
        startActivity(new Intent(getContext(),CalendarActivity.class));
    }

    @Override
    public void showRegistrationView() {
        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registrationFragment);
    }

    @Override
    protected void initViews() {
        Log.d(TAG, "initViews: ");
        emailError = getActivity().findViewById(R.id.error_email_login);
        pwdError = getActivity().findViewById(R.id.error_password_login);
        emailInput = getActivity().findViewById(R.id.edit_email_login);
        pwdInput = getActivity().findViewById(R.id.edit_password_login);
        loginBtn = getActivity().findViewById(R.id.login_btn);
        singUpBtn = getActivity().findViewById(R.id.sing_up_btn);
        loginBtn.setOnClickListener(this);
        singUpBtn.setOnClickListener(this);
        Log.d(TAG, "initViews: "+emailInput.getText().toString());
    }

    @Override
    protected void setUp(Bundle data) {
        //NOT USED
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_btn:
                mPresenter.doLogin(emailInput.getText().toString(),pwdInput.getText().toString(),new String[]{getString(R.string.login_error_email),getString(R.string.login_error_password),getString(R.string.login_error_user_disabled)});
                break;
            case R.id.sing_up_btn:
                mPresenter.openRegistration();
                break;
        }
    }

    private void showCredentialsError(int credentialsValidity){
        switch (credentialsValidity){
            case WRONG_EMAIL_OR_PASSWORD:
                emailError.setText(R.string.wrong_email_login);
                pwdError.setText(R.string.wrong_password_login);
                emailError.setVisibility(View.VISIBLE);
                pwdError.setVisibility(View.VISIBLE);
                break;
            case INVALID_EMAIL_PASSWORD:
                emailError.setText(R.string.invalid_email);
                pwdError.setText(R.string.invalid_password);
                emailError.setVisibility(View.VISIBLE);
                pwdError.setVisibility(View.VISIBLE);
                break;
            case INVALID_PASSWORD:
                pwdError.setText(R.string.invalid_password);
                pwdError.setVisibility(View.VISIBLE);
                emailError.setVisibility(View.INVISIBLE);
                break;
            case INVALID_EMAIL:
                emailError.setText(R.string.invalid_email);
                emailError.setVisibility(View.VISIBLE);
                pwdError.setVisibility(View.INVISIBLE);
                break;
            case USER_DISABLED:
                emailError.setText(R.string.user_disabled);
                emailError.setVisibility(View.VISIBLE);
                pwdError.setVisibility(View.INVISIBLE);
                break;
            case ALL_FIELDS_ARE_VALID:
                emailError.setVisibility(View.INVISIBLE);
                pwdError.setVisibility(View.INVISIBLE);
                break;
        }
    }



    private void newCheckCredentials(int [] credentialsStatus, String [] errorString){
        int c = 0;
        for(int i : credentialsStatus)
            c += i;
        if(c == 0){
            if(credentialsStatus.length > 2){
                ((TextView) getActivity().findViewById(R.id.error_username_sing_up)).setVisibility(View.INVISIBLE);
                ((TextView) getActivity().findViewById(R.id.error_email_sing_up)).setVisibility(View.INVISIBLE);
                ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setVisibility(View.INVISIBLE);
                ((TextView) getActivity().findViewById(R.id.error_password_confirm_sing_up)).setVisibility(View.INVISIBLE);
            }
            else {
                ((TextView) getActivity().findViewById(R.id.error_email_login)).setVisibility(View.INVISIBLE);
                ((TextView) getActivity().findViewById(R.id.error_password_login)).setVisibility(View.INVISIBLE);
            }
            return;
        }
        //HANDLE ERROR
        if(credentialsStatus.length > 2) {
            //REGISTRATION
            if (credentialsStatus[USERNAME] > 0) {
                switch (credentialsStatus[USERNAME]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_username_sing_up)).setText(R.string.invalid_username);
                        break;
                    case SERVER_ERR:
                        ((TextView) getActivity().findViewById(R.id.error_username_sing_up)).setText(errorString[USERNAME]);
                        break;

                }
                ((TextView) getActivity().findViewById(R.id.error_username_sing_up)).setVisibility(View.VISIBLE);
            }
            if (credentialsStatus[EMAIL] > 0) {
                switch (credentialsStatus[EMAIL]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_email_sing_up)).setText(R.string.invalid_email);
                        break;
                    case SERVER_ERR:
                        ((TextView) getActivity().findViewById(R.id.error_email_sing_up)).setText(errorString[EMAIL]);
                        break;

                }
                ((TextView) getActivity().findViewById(R.id.error_email_sing_up)).setVisibility(View.VISIBLE);
            }
            if(credentialsStatus[PWD] > 0){
                switch (credentialsStatus[PWD]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setText(R.string.invalid_email);
                        break;
                    case SERVER_ERR:
                        ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setText(errorString[PWD]);
                        break;
                }
                ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setVisibility(View.VISIBLE);
            }
            if(credentialsStatus[CPWD] > 0){
                switch (credentialsStatus[CPWD]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setText(R.string.invalid_password);
                        ((TextView) getActivity().findViewById(R.id.error_password_sing_up)).setVisibility(View.VISIBLE);
                        ((TextView) getActivity().findViewById(R.id.error_password_confirm_sing_up)).setText(R.string.invalid_confirm_password);
                        break;
                    case NOT_MATCH:
                        ((TextView) getActivity().findViewById(R.id.error_password_confirm_sing_up)).setText(R.string.confirm_password_not_match);
                        break;
                }
                ((TextView) getActivity().findViewById(R.id.error_username_sing_up)).setVisibility(View.VISIBLE);
            }
        }
        else {
            //LOGIN
            if (credentialsStatus[EMAIL_L] > 0) {
                switch (credentialsStatus[EMAIL_L]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_email_login)).setText(R.string.invalid_email);
                        break;
                    case SERVER_ERR:
                        ((TextView) getActivity().findViewById(R.id.error_email_login)).setText(errorString[EMAIL_L]);
                        break;

                }
                ((TextView) getActivity().findViewById(R.id.error_email_login)).setVisibility(View.VISIBLE);
            }
            if (credentialsStatus[PWD_L] > 0) {
                switch (credentialsStatus[PWD_L]){
                    case INVALID_FIELD:
                        ((TextView) getActivity().findViewById(R.id.error_password_login)).setText(R.string.invalid_password);
                        break;
                    case SERVER_ERR:
                        ((TextView) getActivity().findViewById(R.id.error_password_login)).setText(errorString[PWD_L]);
                        break;

                }
                ((TextView) getActivity().findViewById(R.id.error_email_login)).setVisibility(View.VISIBLE);
            }
        }
    }

}
