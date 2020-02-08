package it.guaraldi.to_dotaskmanager.auth;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import it.guaraldi.to_dotaskmanager.util.AppExecutors;

public class SessionManager implements SessionManagerI {

    private static final String USERNAME = "USERNAME";
    private static final String AUTH_TOKEN = "AUTH_TOKEN";
    private Context context;
    private SharedPreferences mSharedPreferences;

    public SessionManager(Context context){
        this.context = context;
        mSharedPreferences = context.getSharedPreferences("SESSION",Context.MODE_PRIVATE);
    }

    @Override
    public void createUserSession(String username, String authToken, Executor io, Executor main, SessionCallback callback) {
       io.execute(new Runnable() {
           @Override
           public void run() {
               SharedPreferences.Editor editor = mSharedPreferences.edit();
               editor.putString(USERNAME,username);
               editor.putString(AUTH_TOKEN,authToken);
               editor.apply();
               boolean result = editor.commit();
               main.execute(new Runnable() {
                   @Override
                   public void run() {
                       if(result)
                           callback.success("User Session SAVED!");
                       else
                           callback.failure("User Session NOT SAVED!");
                   }
               });
           }
       });
    }

    @Override
    public void getUserSession(Executor io,Executor main, SessionCallback callback) {
        io.execute(new Runnable() {
            @Override
            public void run() {
                String username = mSharedPreferences.getString(USERNAME,"UNDEFINED");
                String authToken = mSharedPreferences.getString(AUTH_TOKEN,"UNDEFINED");
                main.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(username.equals("UNDEFINED") && authToken.equals("UNDEFINED"))
                            callback.failure("SESSION NOT EXISTS");
                        else
                            callback.success("Username: " + username + " AuthToken:" + authToken);
                    }
                });
            }
        });
    }

    @Override
    public void deleteUserSession(Executor io, Executor main, SessionCallback callback) {
        io.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.apply();
                boolean result = editor.commit();
                main.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(result)
                            callback.success("User Session REMOVED!");
                        else
                            callback.failure("User Session NOT REMOVED!");
                    }
                });
            }
        });
    }

    @Override
    public void updateUserSession(Executor io, Executor main, String username, String authToken, SessionCallback callback) {
        io.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(USERNAME,username);
                editor.putString(AUTH_TOKEN,authToken);
                editor.apply();
                boolean result = editor.commit();
                main.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(result)
                            callback.success("User Session UPDATED!");
                        else
                            callback.failure("User Session NOT UPDATED!");
                    }
                });
            }
        });
    }


}
