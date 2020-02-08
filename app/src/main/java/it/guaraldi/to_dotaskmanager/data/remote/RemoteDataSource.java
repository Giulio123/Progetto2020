package it.guaraldi.to_dotaskmanager.data.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.internal.firebase_auth.zzey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.zzy;
import com.google.firebase.auth.zzz;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;


import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.User;
import it.guaraldi.to_dotaskmanager.util.AppExecutors;


public class RemoteDataSource implements TasksDataSource {

    private static final String TAG = RemoteDataSource.class.getSimpleName();


    private AppExecutors mAppExecutors;
    private FirebaseAuth mAuth;
    @Inject public RemoteDataSource( AppExecutors appExecutors, FirebaseAuth auth) {
        this.mAppExecutors = appExecutors;
        this.mAuth = auth;
    }

    @Override
    public void authentication(String email, String pwd, FirebaseCallback auth) {
        Log.d(TAG, "authentication: ");
        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(mAppExecutors.networkIO(), task -> mAppExecutors.mainThread().execute(() -> {
            Log.d(TAG, "authentication: THREAD 1");
            if(task.isSuccessful())
                mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(mAppExecutors.networkIO(), task1 -> {
                    Log.d(TAG, "authentication: THREAD 2");
                    if(task1.isSuccessful()) {
                        mAppExecutors.mainThread().execute(() -> {
                            Log.d(TAG, "authentication: ");
                            auth.success(task1);
                        });

                    }
                    else
                        mAppExecutors.mainThread().execute(() -> auth.failure(task1.getException()));
                });
            else
                mAppExecutors.mainThread().execute(() -> auth.failure(task.getException()));
        }));
    }

    @Override
    public void registration(String username, String email, String password, FirebaseCallback callback) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(mAppExecutors.networkIO(), task -> {
            if(task.isSuccessful()) {
                UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();
                mAuth.getCurrentUser().updateProfile(updateProfile).addOnCompleteListener(mAppExecutors.networkIO(), task1 -> {
                    if(task1.isSuccessful())
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(mAppExecutors.networkIO(), task2 -> {
                            if(task2.isSuccessful()){
                                mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(mAppExecutors.networkIO(), task3 -> {
                                    if(task3.isSuccessful()) {
//                                        mAuth.signOut();
                                        mAppExecutors.mainThread().execute(() -> callback.success(task3));
                                    }
                                    else {
                                        mAppExecutors.mainThread().execute(() -> callback.failure(task3.getException()));
                                    }
                                });
                            }
                            else
                                mAppExecutors.mainThread().execute(() -> callback.failure(task2.getException()));
                        });
                    else
                        mAppExecutors.mainThread().execute(() -> callback.failure(task1.getException()));
                });
            }
            else
                mAppExecutors.mainThread().execute(() -> callback.failure(task.getException()));
        });
    }

    @Override
    public void reloadUser(FirebaseCallback callback) {
        mAuth.getCurrentUser().reload().addOnCompleteListener(mAppExecutors.networkIO(), task -> {
            if(task.isSuccessful())
                mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(mAppExecutors.networkIO(), task1 -> {
                    if(task1.isSuccessful())
                        mAppExecutors.mainThread().execute(() -> callback.success(task1));
                    else
                        mAppExecutors.mainThread().execute(() -> callback.failure(task1.getException()));
                });
            else
                mAppExecutors.mainThread().execute(() -> callback.failure(task.getException()));
        });
    }

    @Override
    public void deleteCurrentUser(FirebaseCallback callBack) {
        mAuth.getCurrentUser().delete().addOnCompleteListener(mAppExecutors.networkIO(), task -> {
            if(task.isSuccessful())
                mAppExecutors.mainThread().execute(() -> callBack.success(task));
            else
                mAppExecutors.mainThread().execute(() -> callBack.failure(task.getException()));
        });
    }


    @Override
    public void getCurrentUser(LoadSessionCallback callback) {
        if(mAuth.getCurrentUser()!=null)
            callback.success(new User(mAuth.getCurrentUser()));
        else
            callback.failure();
    }

    @Override
    public void signOut(SignOutCallback callback) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null)
                    mAuth.signOut();
                    mAppExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.success();
                        }
                    });

            }
        });
    }

    @Override
    public void createTask(it.guaraldi.to_dotaskmanager.data.local.entities.Task newTask, DBCallback callback) {

    }

    @Override
    public void changeTaskStatus(String status, int taskId, DBCallback callback) {

    }

    @Override
    public void getLastTaskId(DBCallbackId callback) {

    }

    @Override
    public void getSizeTableTaks(DBCallbackSize callback) {

    }

    @Override
    public void getAllTasks(DBCallBackTasks callBackTaks) {

    }

    @Override
    public void getUpcommingTasks(long startDate, long endDate, DBCallBackTasks callBackTasks) {
    }

    @Override
    public void deleteTask(int taskId, DBCallback callback) {

    }

    @Override
    public void deleteAllTasks(DBCallback callback) {

    }

    @Override
    public void getTaskById(int taskId, DBCallBackTasks callBackTasks) {

    }

    @Override
    public void reauthentication(String email, String password, FirebaseCallback callback) {
        Log.d(TAG, "reauthentication: ");
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(mAppExecutors.networkIO(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    mAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(mAppExecutors.networkIO(), new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful())
                                    callback.success(task);
                                else
                                    callback.failure(task.getException());

                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    @Override
    public void getAuthToken(LoadStringCallback callback) {
        Log.d(TAG, "getAuthToken: ");
//        mUtilAccounts.getAuthToken(AccountManager.KEY_ACCOUNT_TYPE, mAppExecutors.networkIO(), mAppExecutors.mainThread(), new LoadStringCallback() {
//            @Override
//            public void success(String result) {
//                callback.success(result);
//            }
//
//            @Override
//            public void failure(Exception e) {
//                callback.failure(e);
//            }
//        });
    }

    @Override
    public void addCategory(String category, LoadCategory loadCategory) {
        //DONT WORK
    }

    @Override
    public void loadCategories(LoadCategories loadCategories) {
        //DONT WORK
    }

    @Override
    public void deleteCategory(String category, LoadCategory loadCategory) {
        //DONT WORK
    }
}
