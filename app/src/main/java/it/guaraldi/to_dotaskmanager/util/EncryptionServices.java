package it.guaraldi.to_dotaskmanager.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyPermanentlyInvalidatedException;

import androidx.annotation.Nullable;

import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

public class EncryptionServices {

    public static String DEFAULT_KEY_STORE_NAME = "default_keystore";
    public static String MASTER_KEY = "MASTER_KEY";
    public static String FINGERPRINT_KEY = "FINGERPRINT_KEY";
    public static String CONFIRM_CREDENTIALS_KEY = "CONFIRM_CREDENTIALS_KEY";
    public static byte [] KEY_VALIDATION_DATA = new byte[]{0,1,0,1};
    public static int CONFIRM_CREDENTIALS_VALIDATION_DELAY = 120;
    private Context context;
    private KeyStoreWrapper keyStoreWrapper;


    public EncryptionServices(Context context){
        this.context = context;
        this.keyStoreWrapper = new KeyStoreWrapper(context,DEFAULT_KEY_STORE_NAME);
    }

    /**
     * Encryption Stage
     */

    /**
     * Create and save cryptography key, to protect Secrets with.
     */
    public void createMasterKey(String password){
        if (SystemServices.hasMarshmallow())
            createAndroidSymmetricKey();
        else
            createDefaultSymmetricKey(password == null ? password : "");
    }

    /**
     * Remove master cryptography key. May be used for re sign up functionality.
     */
    public void removeMasterKey(){
        keyStoreWrapper.removeAndroidKeyStoreKey(MASTER_KEY);
    }


    /**
     * Encrypt user password and Secrets with created master key.
     */
    public String encrypt(String data, @Nullable String keyPassword){
        return SystemServices.hasMarshmallow() ?   encryptWithAndroidSymmetricKey(data) :
                encryptWithDefaultSymmetricKey(data, keyPassword!=null ? keyPassword: "");
    }

    /**
     * Decrypt user password and Secrets with created master key.
     */
    public String decrypt(String data, @Nullable String keyPassword){
        return SystemServices.hasMarshmallow() ? decryptWithAndroidSymmetricKey(data) :
                decryptWithDefaultSymmetricKey(data, keyPassword!=null ? keyPassword: "");
    }

    private void createAndroidSymmetricKey(){
        keyStoreWrapper.createAndroidKeyStoreSymmetricKey(MASTER_KEY);
    }

    private String encryptWithAndroidSymmetricKey(String data){
        SecretKey masterKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(MASTER_KEY);
        return new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).encrypt(data, masterKey, true);
    }

    private String decryptWithAndroidSymmetricKey(String data){
        SecretKey masterKey = keyStoreWrapper.createAndroidKeyStoreSymmetricKey(MASTER_KEY);
        return new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).decrypt(data, masterKey,true);
    }

    private void createDefaultSymmetricKey(String password){
        keyStoreWrapper.createDefaultKeyStoreSymmetricKey(MASTER_KEY,password);
    }

    private String encryptWithDefaultSymmetricKey(String data, String keyPassword){
        SecretKey masterKey = keyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY,keyPassword);
        return new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).encrypt(data,masterKey,true);
    }

    private String decryptWithDefaultSymmetricKey(String data, String keyPassword){
        SecretKey masterKey = keyStoreWrapper.getDefaultKeyStoreSymmetricKey(MASTER_KEY,keyPassword);
        return new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).decrypt(data,masterKey,true);
    }

    /**
     * Fingerprint Stage
     */

    /**
     * Create and save cryptography key, that will be used for fingerprint authentication.
     */
    public void createFingerprintKey(){
        if(SystemServices.hasMarshmallow())
            keyStoreWrapper.createAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY,
                    true,
                    true,
                    false);
    }


    /**
     * Remove fingerprint authentication cryptographic key.
     */
    public void removeFingerprintKey(){
        if(SystemServices.hasMarshmallow())
            keyStoreWrapper.removeAndroidKeyStoreKey(FINGERPRINT_KEY);
    }

    /**
     * @return initialized crypto object or null if fingerprint key was invalidated or not created yet.
     */
    public @Nullable FingerprintManager.CryptoObject prepareFingerprintCryptoObject(){


        if(SystemServices.hasMarshmallow()){
            try {
                SecretKey symmetricKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(FINGERPRINT_KEY);
                Cipher cipher = new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC).cipher;
                cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
                return new FingerprintManager.CryptoObject(cipher);
            }catch (Throwable e){
                // VerifyError will be thrown on API lower then 23 if we will use unedited
                // class reference directly in catch block
                if(e instanceof KeyPermanentlyInvalidatedException || e instanceof  IllegalArgumentException)
                    return null;
                    // Fingerprint key was not generated
                else if(e instanceof InvalidKeyException)
                    return null;
                try {
                    throw e;
                } catch (InvalidKeyException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @return true if cryptoObject was initialized successfully and key was not invalidated during authentication.
     */

    @TargetApi(23)
    public boolean validateFingerprintAuthentication(FingerprintManager.CryptoObject cryptoObject) {
        try {
            cryptoObject.getCipher().doFinal(KEY_VALIDATION_DATA);
            return true;
        }catch (Throwable e){
            // VerifyError is will be thrown on API lower then 23 if we will use unedited
            // class reference directly in catch block
            if(e instanceof IllegalBlockSizeException)
                return false;
            try {
                throw  e;
            } catch (BadPaddingException e1) {
                e1.printStackTrace();
            } catch (IllegalBlockSizeException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Confirm Credential Stage
     */

    /**
     * Create and save cryptography key, that will be used for confirm credentials authentication.
     */
    public void createConfirmCredentialsKey(){
        if(SystemServices.hasMarshmallow())
            keyStoreWrapper.createAndroidKeyStoreSymmetricKey(CONFIRM_CREDENTIALS_KEY,
                    true,CONFIRM_CREDENTIALS_VALIDATION_DELAY);
    }

    /**
     * Remove confirm credentials authentication cryptographic key.
     */
    public void removeConfirmCredentialsKey(){
        keyStoreWrapper.removeAndroidKeyStoreKey(CONFIRM_CREDENTIALS_KEY);
    }

    /**
     * @return true if confirm credential authentication is not required.
     */
    public boolean validateConfirmCredentialsAuthentication(){
        if(!SystemServices.hasMarshmallow())
            return true;
        SecretKey symmetricKey = keyStoreWrapper.getAndroidKeyStoreSymmetricKey(CONFIRM_CREDENTIALS_KEY);
        CipherWrapper cipherWrapper = new CipherWrapper(CipherWrapper.TRANSFORMATION_SYMMETRIC);

        if(symmetricKey != null) {
            cipherWrapper.encrypt(KEY_VALIDATION_DATA.toString(), symmetricKey);
            return true;
        }
        else
            return false;
    }
}
