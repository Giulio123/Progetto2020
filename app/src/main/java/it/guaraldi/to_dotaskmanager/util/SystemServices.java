package it.guaraldi.to_dotaskmanager.util;

import android.accessibilityservice.FingerprintGestureController;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.BuildConfig;

@TargetApi(Build.VERSION_CODES.M)
public  class SystemServices {
    private static Context context;
    private static KeyguardManager keyguardManager;
    /**
     * There is a nice [FingerprintManagerCompat] class that makes all dirty work for us, but as always, shit happens.
     * Behind the scenes it is using `Context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)`
     * method, that is returning false on 23 API emulators, when in fact [FingerprintManager] is there and is working fine.
     */
    private FingerprintManager fingerprintManager = null;

    public SystemServices(Context context){
        this.context = context;
        this.keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if(hasMarshmallow())
            this.fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
    }

    public static boolean hasMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isDeviceSecure(){
        return hasMarshmallow()? keyguardManager.isDeviceSecure():keyguardManager.isKeyguardSecure();
    }

    public boolean isFingerprintHardwareAvailable(){
        return fingerprintManager!= null && fingerprintManager.isHardwareDetected();
    }

    public boolean hasEnrolledFingerprints(){

        return fingerprintManager != null && fingerprintManager.hasEnrolledFingerprints();
    }

    public void authenticateFingerprint(FingerprintManager.CryptoObject cryptoObject, CancellationSignal cancellationSignal,
                                       int flags, FingerprintManager.AuthenticationCallback callback, @Nullable Handler handler){
        if(fingerprintManager != null)
        fingerprintManager.authenticate(cryptoObject,cancellationSignal,flags,callback,handler);
    }

    public void showAuthenticationScreen(Activity activity, int requestCode,@Nullable String title,@Nullable String description){
       // Create the Confirm Credentials screen. You can customize the title and description. Or
        // we will provide a generic one for you if you leave it null
       if(hasMarshmallow()){
           Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(title,description);
           if(intent != null)
               activity.startActivityForResult(intent,requestCode);
       }
    }

    // Used to block application if no lock screen is setup.
    public static AlertDialog showDeviceSecurityAlert(){
        return new AlertDialog.Builder(context)
                .setTitle("LOCK TITLE")
                .setMessage("LOCK MESSAGGE")
                .setPositiveButton("POSITIVE", (dialogInterface, i) -> ((ContextToDo)context).openLockScreenSettings())
                .setNegativeButton("NEGATIVE", (dialogInterface, i) -> System.exit(0))
                .setCancelable(BuildConfig.DEBUG)
                .show();
}

}
