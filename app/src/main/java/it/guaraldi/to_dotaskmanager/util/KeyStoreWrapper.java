package it.guaraldi.to_dotaskmanager.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

public class KeyStoreWrapper {
    private Context context;
    private KeyStore keyStore;
    private File defaultKeyStoreFile;
    private KeyStore defaultKeyStore;

    public KeyStoreWrapper(Context context, String defaultKeyStoreName){
        this.context =context;
        this.defaultKeyStoreFile = new File(context.getFilesDir(), defaultKeyStoreName);
        this.keyStore = createAndroidKeyStore();
        this.defaultKeyStore = createDefaultKeyStore();
    }

    /**
     * ANDROID KEYSTORE
     */

    //Remember this.keyStore = createAndroidKeyStore()
    private KeyStore createAndroidKeyStore(){

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public @Nullable SecretKey getAndroidKeyStoreSymmetricKey(String alias){
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey) keyStore.getKey(alias,null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return secretKey;
    }

    public @Nullable KeyPair getAndroidKeyStoreAsymmetricKeyPair(String alias){
        PrivateKey privateKey = null;
        try {
            privateKey = (PrivateKey) keyStore.getKey(alias,null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        PublicKey publicKey = null;
        try {
            publicKey = (PublicKey) keyStore.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        if(privateKey != null && publicKey != null)
            return new KeyPair(publicKey,privateKey);
        else
            return null;
    }

    public void removeAndroidKeyStoreKey(String alias){
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    // KeyProperties settings:KEY_ALGORITHM_AES|BLOCK_MODE_CBC|ENCRYPTION_PADDING_PKCS7
    @TargetApi(23)
    public @Nullable SecretKey createAndroidKeyStoreSymmetricKey(String alias){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            KeyGenParameterSpec.Builder builder =
                    new KeyGenParameterSpec.Builder(alias,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT )
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return keyGenerator.generateKey();
    }

    @TargetApi(23)
    public @Nullable SecretKey createAndroidKeyStoreSymmetricKey(String alias, boolean userAuthenticationRequired,
                                                                 int userAuthenticationValidityDurationSeconds){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use of the key
                    .setUserAuthenticationRequired(userAuthenticationRequired)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationValidityDurationSeconds(userAuthenticationValidityDurationSeconds)
                    //Not working on api 23, try higher ?
                    .setRandomizedEncryptionRequired(false);
            keyGenerator.init(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return keyGenerator.generateKey();
    }
    @TargetApi(23)
    public @Nullable SecretKey createAndroidKeyStoreSymmetricKey(String alias, boolean userAuthenticationRequired,
                                                                 boolean invalidatedByBiometricEnrollment, boolean userAuthenticationValidWhileOnBody) {

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use of the key
                    .setUserAuthenticationRequired(userAuthenticationRequired)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    //Not working on api 23, try higher ?
                    .setRandomizedEncryptionRequired(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
                builder.setUserAuthenticationValidWhileOnBody(userAuthenticationValidWhileOnBody);
            }

            keyGenerator.init(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return keyGenerator.generateKey();
    }
    @TargetApi(Build.VERSION_CODES.M)
    public @Nullable SecretKey createAndroidKeyStoreSymmetricKey(
            String alias, boolean userAuthenticationRequired, boolean invalidatedByBiometricEnrollment,
            int userAuthenticationValidityDurationSeconds, boolean userAuthenticationValidWhileOnBody){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use of the key
                    .setUserAuthenticationRequired(userAuthenticationRequired)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationValidityDurationSeconds(userAuthenticationValidityDurationSeconds)
                    //Not working on api 23, try higher ?
                    .setRandomizedEncryptionRequired(false);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
                builder.setUserAuthenticationValidWhileOnBody(userAuthenticationValidWhileOnBody);
            }

            keyGenerator.init(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return keyGenerator.generateKey();
    }


    /**
     * DEFAUL KEYSTORE
     */
    //Remember this.defaultKeyStore = createDefaultKeyStore()
    private KeyStore createDefaultKeyStore(){
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            if (!defaultKeyStoreFile.exists())
                keyStore.load(null);
            else
                keyStore.load(new FileInputStream(defaultKeyStoreFile), null);
            return keyStore;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createDefaultKeyStoreSymmetricKey(String alias, String password){
        SecretKey key = generateDefaultSymmetricKey();
        KeyStore.Entry keyEntry = new KeyStore.SecretKeyEntry(key);
        try {
            defaultKeyStore.setEntry(alias,keyEntry,new KeyStore.PasswordProtection(password.toCharArray()));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            defaultKeyStore.store(new FileOutputStream(defaultKeyStoreFile), password.toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public @Nullable SecretKey getDefaultKeyStoreSymmetricKey(String alias, String keyPassword){
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey) defaultKeyStore.getKey(alias,keyPassword.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return secretKey;
    }


    /**
     * GENERATE KEY
     */

    //DEFAULT PROVIDER
    //KeyProperties.KEY_ALGORITHM_AES | KeyProperties.BLOCK_MODE_CBC | KeyProperties.ENCRYPTION_PADDING_PKCS7
    private SecretKey generateDefaultSymmetricKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            return keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }







    //KeyProperties settings: KEY_ALGORITHM_RSA| BLOCK_MODE_ECB|ENCRYPTION_PADDING_RSA_PKCS1
    @TargetApi(Build.VERSION_CODES.M)
    public @Nullable KeyPair createAndroidKeyStoreAsymmetricKey(String alias){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            if (SystemServices.hasMarshmallow())
                initGeneratorWithKeyGenParameterSpec(generator, alias);
//            else
//                initGeneratorWithKeyPairGeneratorSpec(generator, alias);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initGeneratorWithKeyPairGeneratorSpec(KeyPairGenerator generator, String alias){
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR,20);
        KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(new X500Principal("CN="+alias+" CA Certificate"))
                .setStartDate(startDate.getTime())
                .setEndDate(endDate.getTime());
        try {
            generator.initialize(builder.build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initGeneratorWithKeyGenParameterSpec(KeyPairGenerator generator, String alias){
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
        try {
            generator.initialize(builder.build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }



}
