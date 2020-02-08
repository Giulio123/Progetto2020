package it.guaraldi.to_dotaskmanager.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.concurrent.Executor;

import it.guaraldi.to_dotaskmanager.auth.User;
import it.guaraldi.to_dotaskmanager.data.TasksDataSource;

public class UtilAccounts {
    private Context context;
    private AccountManager am;
    public UtilAccounts(Context context){
        this.context = context;
        am = AccountManager.get(context);

    }
//
//    public void addAccount(String username, String password, String token, Parcelable userData){
//        User user = null;
//        if(userData instanceof FirebaseUser)
//            user = new User((FirebaseUser) userData, token);
//        Bundle accountData = new Bundle();
//        accountData.putParcelable("USER_DATA",userData);
//        Account account = new Account(username,AccountManager.KEY_ACCOUNT_TYPE);
//        am.addAccountExplicitly(account,password,accountData);
//        am.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,token);
//    }
//
//    public void getAuthToken(String accountType , Executor network, Executor main, TasksDataSource.LoadStringCallback callback){
//        network.execute(() -> {
//            try {
//                Account account = am.getAccountsByType(accountType)[0];
//                String authToken = am.blockingGetAuthToken(account,AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS,true);
//                am.invalidateAuthToken(accountType,authToken);
//                am.getAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, (Activity) context, accountManagerFuture ->
//                    main.execute(() -> {
//                        try {
//                            if(accountManagerFuture.isDone() && accountManagerFuture.getResult() != null) {
//                                String authToken1 = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
//                                callback.success(authToken1);
//                            }
//                            else
//                                callback.failure(new ClassCastException("Problem with token"));
//                        } catch (AuthenticatorException |IOException|OperationCanceledException e) {
//                            callback.failure(e);
//                        }
//                    })
//                , null);
//            } catch (AuthenticatorException | IOException |OperationCanceledException e) {
//                main.execute(() -> callback.failure(e));
//            }
//
//        });
//    }
}
