package it.guaraldi.to_dotaskmanager.util;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public abstract class ContextToDo extends Context {
    public void openLockScreenSettings(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        startActivity(intent);
    }

    public void openSecureSettings(){
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
    }
}
