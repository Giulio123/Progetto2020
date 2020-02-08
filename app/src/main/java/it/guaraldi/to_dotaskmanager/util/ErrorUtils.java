package it.guaraldi.to_dotaskmanager.util;

import android.content.Context;

import it.guaraldi.to_dotaskmanager.R;

public class ErrorUtils {
    private String [] usernameServerError;
    private String [] emailServerError;
    private String [] pwdServerError;
    private Context context;

    public ErrorUtils(Context context){
        this.context = context;
        usernameServerError = context.getResources().getStringArray(R.array.username_errors);
        emailServerError = context.getResources().getStringArray(R.array.email_errors);
        pwdServerError = context.getResources().getStringArray(R.array.pwd_errors);
    }



}
