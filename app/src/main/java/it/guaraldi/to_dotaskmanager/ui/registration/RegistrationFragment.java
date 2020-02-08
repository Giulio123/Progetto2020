package it.guaraldi.to_dotaskmanager.ui.registration;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;

import android.security.keystore.KeyInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.security.KeyPair;


import javax.inject.Inject;
import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;
import it.guaraldi.to_dotaskmanager.util.CipherWrapper;
import it.guaraldi.to_dotaskmanager.util.EncryptionServices;
import it.guaraldi.to_dotaskmanager.util.KeyStoreWrapper;
import it.guaraldi.to_dotaskmanager.util.SystemServices;

import static android.app.Activity.RESULT_OK;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.ALL_FIELDS_ARE_INVALID;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.ALL_FIELDS_ARE_VALID;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.DIALOG_EMAIL_VERIFICATION_REQ;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_EMAIL;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_EMAIL_PASSWORD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_PASSWORD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_USERNAME;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_USERNAME_EMAIL;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.INVALID_USERNAME_PASSWORD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.USERNAME_TAKEN;

public class RegistrationFragment extends BaseFragment implements RegistrationContract.View, View.OnClickListener, TextWatcher {
    @Inject RegistrationPresenter mPresenter;
    private static final String TAG = "RegistrationFragment";
    private TextView errorUsernameTv;
    private TextView errorEmailTv;
    private TextView errorPwdTv;
    private TextView errorConfirmPwdTv;
    private EditText usernameInput;
    private EditText emailInput;
    private EditText pwdInput;
    private EditText confirmPwdInput;
    private Button singUpBtn;
    private Button loginBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        return inflater.inflate(R.layout.registration_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initViews();
        cryptString("Hello Word");
    }


    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void updateViewData(boolean userRegistration, String token, int errorType) {

        if(userRegistration && token != null) {
            Snackbar.make(getView(), "User has been registered!", Snackbar.LENGTH_SHORT).show();
            showcredentialsError(ALL_FIELDS_ARE_VALID);
        }
        if(!userRegistration && token == null){
            showcredentialsError(errorType);
        }
    }

    @Override
    public void showLoginView() {
        Navigation.findNavController(getView()).navigate(R.id.action_registrationFragment_to_loginFragment);
    }

    @Override
    protected void initViews() {
        errorUsernameTv = getActivity().findViewById(R.id.error_username_sing_up);
        errorEmailTv = getActivity().findViewById(R.id.error_email_sing_up);
        errorPwdTv = getActivity().findViewById(R.id.error_password_sing_up);
        errorConfirmPwdTv = getActivity().findViewById(R.id.error_password_confirm_sing_up);
        usernameInput = getActivity().findViewById(R.id.edit_username_sing_up);
        usernameInput.addTextChangedListener(this);
        emailInput = getActivity().findViewById(R.id.edit_email_sing_up);
        pwdInput = getActivity().findViewById(R.id.edit_password_sing_up);
        confirmPwdInput = getActivity().findViewById(R.id.edit_password_confirm_sing_up);
        singUpBtn = getActivity().findViewById(R.id.sing_up_btn_sing_up);
        loginBtn = getActivity().findViewById(R.id.login_btn_sing_up);
       errorUsernameTv.setOnClickListener(this);
       errorEmailTv.setOnClickListener(this);
       errorPwdTv.setOnClickListener(this);
       errorConfirmPwdTv.setOnClickListener(this);
       usernameInput.setOnClickListener(this);
       emailInput.setOnClickListener(this);
       pwdInput.setOnClickListener(this);
       confirmPwdInput.setOnClickListener(this);
       singUpBtn.setOnClickListener(this);
       loginBtn.setOnClickListener(this);
    }

    @Override
    protected void setUp(Bundle data) {

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: "+getResources().getResourceName(v.getId()));
        switch (v.getId()){
            case R.id.sing_up_btn_sing_up:
                if(validateConfirmPwd())
                    mPresenter.doRegistration(
                        usernameInput.getText().toString(),
                        emailInput.getText().toString(),
                        pwdInput.getText().toString(),new String[]{getString(R.string.registration_error_user_taken)});
                break;
            case R.id.login_btn_sing_up:
                mPresenter.openLogin();
                break;
        }
    }

    private void showcredentialsError(int credentialsValidity){
        switch (credentialsValidity){
            case INVALID_USERNAME:
                errorUsernameTv.setText(R.string.invalid_username);
                errorUsernameTv.setVisibility(View.VISIBLE);
                errorEmailTv.setVisibility(View.INVISIBLE);
                errorPwdTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
            case INVALID_EMAIL:
                errorEmailTv.setText(R.string.invalid_email);
                errorEmailTv.setVisibility(View.VISIBLE);
                errorUsernameTv.setVisibility(View.INVISIBLE);
                errorPwdTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
            case INVALID_PASSWORD:
                errorPwdTv.setText(R.string.invalid_password);
                errorPwdTv.setVisibility(View.VISIBLE);
                if(validateConfirmPwd()){
                    errorConfirmPwdTv.setText(R.string.invalid_confirm_password);
                    errorConfirmPwdTv.setVisibility(View.VISIBLE);
                }
                errorEmailTv.setVisibility(View.INVISIBLE);
                errorUsernameTv.setVisibility(View.INVISIBLE);
                break;
            case INVALID_USERNAME_EMAIL:
                errorUsernameTv.setText(R.string.invalid_username);
                errorUsernameTv.setVisibility(View.VISIBLE);
                errorEmailTv.setText(R.string.invalid_email);
                errorEmailTv.setVisibility(View.VISIBLE);
                errorPwdTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
            case INVALID_USERNAME_PASSWORD:
                errorUsernameTv.setText(R.string.invalid_username);
                errorUsernameTv.setVisibility(View.VISIBLE);
                errorPwdTv.setText(R.string.invalid_password);
                errorPwdTv.setVisibility(View.VISIBLE);
                if(validateConfirmPwd()){

                    errorConfirmPwdTv.setText(R.string.invalid_confirm_password);
                    errorConfirmPwdTv.setVisibility(View.VISIBLE);
                }
                errorEmailTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
            case INVALID_EMAIL_PASSWORD:
                errorEmailTv.setText(R.string.invalid_email);
                errorEmailTv.setVisibility(View.VISIBLE);
                errorPwdTv.setText(R.string.invalid_password);
                errorPwdTv.setVisibility(View.VISIBLE);
                if(validateConfirmPwd()){
                    errorConfirmPwdTv.setText(R.string.invalid_confirm_password);
                    errorConfirmPwdTv.setVisibility(View.VISIBLE);
                }
                errorUsernameTv.setVisibility(View.INVISIBLE);
                break;
            case ALL_FIELDS_ARE_INVALID:
                errorUsernameTv.setText(R.string.invalid_username);
                errorUsernameTv.setVisibility(View.VISIBLE);
                errorEmailTv.setText(R.string.invalid_email);
                errorEmailTv.setVisibility(View.VISIBLE);
                errorPwdTv.setText(R.string.invalid_password);
                errorPwdTv.setVisibility(View.VISIBLE);
                if(validateConfirmPwd()){
                    errorConfirmPwdTv.setText(R.string.invalid_confirm_password);
                    errorConfirmPwdTv.setVisibility(View.VISIBLE);
                }
                break;
            case ALL_FIELDS_ARE_VALID:
                errorUsernameTv.setVisibility(View.INVISIBLE);
                errorEmailTv.setVisibility(View.INVISIBLE);
                errorPwdTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
            case USERNAME_TAKEN:
                errorEmailTv.setText(R.string.username_taken);
                errorEmailTv.setVisibility(View.VISIBLE);
                errorUsernameTv.setVisibility(View.INVISIBLE);
                errorPwdTv.setVisibility(View.INVISIBLE);
                validateConfirmPwd();
                break;
        }
    }

    private boolean validateConfirmPwd(){
        boolean validation = pwdInput.getText().toString().equals(confirmPwdInput.getText().toString());
        errorConfirmPwdTv.setVisibility(validation? View.INVISIBLE : View.VISIBLE);
        if(!validation)
            errorConfirmPwdTv.setText(R.string.confirm_password_not_match);
        return validation;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        https://stackoverflow.com/questions/11134144/android-edittext-onchange-listener
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }



    private void cryptString(String data){

        // Creates Android Key Store and provides manage functions
        KeyStoreWrapper keyStoreWrapper = new KeyStoreWrapper(getContext(), EncryptionServices.DEFAULT_KEY_STORE_NAME);

        // Create and Save asymmetric key
        keyStoreWrapper.createAndroidKeyStoreAsymmetricKey("MASTER_KEY");

        // Get key from keyStore
        KeyPair masterKey = keyStoreWrapper.getAndroidKeyStoreAsymmetricKeyPair("MASTER_KEY");

        // Creates Cipher with given transformation and provides encrypt and decrypt functions
        CipherWrapper cipherWrapper = new CipherWrapper(CipherWrapper.TRANSFORMATION_ASYMMETRIC);

        Log.d(TAG, "cryptString: data:"+data);

        // Encrypt message with the key, using public key
        String encryptedData = cipherWrapper.encrypt(data, masterKey.getPublic());
        Log.d(TAG, "cryptString: encryptedData:\n"+encryptedData);

        String decryptedData = cipherWrapper.decrypt(encryptedData, masterKey.getPrivate());
        Log.d(TAG, "cryptString: decryptedData:" + decryptedData);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch (requestCode) {
//            case DIALOG_EMAIL_VERIFICATION_REQ:
//                if (resultCode == Activity.RESULT_OK)
//                    Log.d(TAG, "onActivityResult: EMAIL VERIFICATION SUCCESS");
//                else
//                    Log.d(TAG, "onActivityResult: EMAIL CERTIFICATION FAILURE");
//                break;
//            case AccountGeneral.REQ_REGISTRATION:
//                if (resultCode == Activity.RESULT_OK)
//                    Log.d(TAG, "onActivityResult: REQ_REGISTRATIONNNNNN");
//                else
//                    Log.d(TAG, "onActivityResult: REQ_REGISTRATION FAILURE");
//                break;
//            case AccountGeneral.REQ_LOGIN:
//                if (resultCode == Activity.RESULT_OK)
//                    Log.d(TAG, "onActivityResult: LOGIN SUCCESS");
//                else
//                    Log.d(TAG, "onActivityResult: LOGIN FAILURE");
//                break;
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//        }
//
//    }
}