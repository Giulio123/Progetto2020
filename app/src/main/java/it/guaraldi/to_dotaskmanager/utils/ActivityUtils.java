package it.guaraldi.to_dotaskmanager.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import retrofit2.http.PUT;

public class ActivityUtils {
    //costant
    public static final int PERIOD_POS_NOT_FOUND = -1;
    public static final int DONT_REPEAT_POS = 0;
    public static final int EVERY_DAY_POS = 1;
    public static final int EVERY_WEEK_POS = 2;
    public static final int EVERY_MONTH_POS = 3;
    public static final int EVERY_YEAR_POS = 4;
    public static final int PERSONALIZED_POS = 5;
    public static final String POS = "POSITION";
    public static final String PERSONALIZED_PERIOD = "PERSONALIZED_PERIOD";
    public static final String PERIOD = "PERIOD";
    public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";
    public static final String LIST_CATEGORIES = "CATEGORIES";
    public static final String LONG_CLICK = "LONG_CLICK";
    // PARMS FOR FRAGMENT
    public static final String EDIT_STATE = "EDIT_STATE";
    public static final String PERSONALIZED_STATE = "PERSONALIZED_STATE";
    public static final String CHILD_PERSONALIZED_STATE = "CHILD_PERSONALIZED_STATE";
    public static final String PARAMS = "PARAMS";
    public static final String PERIOD_PARAMS = "PERIOD_PARAMS";
    public static final String CATEGORY_PARAMS = "CATEGORY_PARAMS";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String REPLY_TASK = "REPLY_TASK";
    //TYPE REQ OF activityForResult
    public static final int CHILD_PERS_REQ = 5;
    public static final int DIALOG_PERIOD_REQ = 3;
    public static final int DIALOG_ADD_CATEGORY_REQ = 7;
    public static final int DIALOG_CATEGORY_REQ = 9;
    public static final int REMOVE_CATEGORY = 11;
    public static final int LOAD_CATEGORY = 13;
    public static final int DIALOG_EMAIL_VERIFICATION_REQ = 15;
    public static final int REPETITION_CHANGED_REQ = 17;
    public static final String CATEGORY = "NEW_CATEGORY";
    //ERROR REGISTRATION & LOGIN
    public static final int INVALID_USERNAME = 0;
    public static final int INVALID_EMAIL = 1;
    public static final int INVALID_PASSWORD = 2;
    public static final int INVALID_USERNAME_EMAIL = 3;
    public static final int INVALID_USERNAME_PASSWORD = 4;
    public static final int INVALID_EMAIL_PASSWORD = 5;
    public static final int ALL_FIELDS_ARE_INVALID = 6;
    public static final int ALL_FIELDS_ARE_VALID = 7;
    public static final int PASSWORD_DONT_MATCH = 8;
    public static final int USERNAME_TAKEN = 9;
    public static final int USER_DISABLED = 11;
    public static final int WRONG_EMAIL_OR_PASSWORD = 10;


}
