package it.guaraldi.to_dotaskmanager.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String TAG = "DateUtils";
    public static final SimpleDateFormat formatDayMonthText = new SimpleDateFormat("EEE MMM d",Locale.ENGLISH);
    public static final SimpleDateFormat formatDataText = new SimpleDateFormat("E dd MMM yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat formatTimeText = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
    public static final SimpleDateFormat formatDateLastDayText = new SimpleDateFormat("d MMM  yyyy",Locale.ENGLISH);
    public static final SimpleDateFormat formatDateRepeatedField = new SimpleDateFormat("MMM d",Locale.ENGLISH);
    public static final SimpleDateFormat formatCompleteInformation = new SimpleDateFormat("EEE d MMM HH:mm:ss yyyy",Locale.ENGLISH);
    public static final SimpleDateFormat formatMillisec = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS",Locale.ENGLISH);
    public static final SimpleDateFormat formatMonthTitle = new SimpleDateFormat("MMMM", /*Locale.ENGLISH*/Locale.ENGLISH);

    public static String formatDate(Date date){
        String res = formatDataText.format(date);
        res = res.substring(0,1).toUpperCase().concat(res.substring(1));
        return res;
    }
    public static String formatDateDayMonth(Date date){
        String res = formatDayMonthText.format(date);
        return res;
    }

    public static String longToStringCalendarDate(long date){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatDate(c.getTime());
    }


    public static String formatTime(Date date){
        return formatTimeText.format(date);
    }
    public static String formatLastDay(Date date){
        return formatDateLastDayText.format(date);
    }
    public static String formatRepeatedField(Date date){return formatDateRepeatedField.format(date);}
    public static String formatCompleteInformationDate(Date date){return formatCompleteInformation.format(date);}
    public static String formatMonthTitle(Date date){return formatMonthTitle.format(date);}

    public static Date parseDate(String date){
        Date res = null;
        try {
            Log.d("CODDIO", "parseDate---> prima:"+date);
            res = formatDataText.parse(date);
            Log.d("CODDIO", "parseDate---> dopo:"+date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Date parseTime(String date){
        Date res = null;
        try {
            res = formatTimeText.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Date parseLastDay(String date){
        Date res = null;
        try {
            res = formatDateLastDayText.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Date parseDateRepeatedField(String date){
        Date res = null;
        try {
            res = formatDateRepeatedField.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Date parseCompleteInformationDate(String date){
        Date res = null;
        try{
            res = formatCompleteInformation.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return res;
    }

    public static Calendar stringToCalendarDate(String date){
        date = date.substring(0,1).toLowerCase().concat(date.substring(1));
        Log.d("CODDIO", "stringToCalendarDate: String:"+date);
        Calendar c = Calendar.getInstance();
        Date d = parseDate(date);
        Log.d("CODDIO", "stringToCalendarDate: Date:"+date);
        c.setTime(d);
        Log.d("CODDIO", "stringToCalendarDate: Calendar:"+c.getTime());
        return c;
    }

    public static Calendar stringToCalendarTime(String date){
        Calendar c = Calendar.getInstance();
        c.setTime(parseTime(date));
        return c;
    }

    public static Calendar stringToCalendarLastDay(String date){
        Calendar c  = Calendar.getInstance();
        c.setTime(parseLastDay(date));
        return c;
    }

    public static Calendar stringToCalendarCompleteDate(String date){
        date = date.substring(0,1).toLowerCase().concat(date.substring(1));
        Calendar c =Calendar.getInstance();
        Date d = parseCompleteInformationDate(date);
        c.setTime(d);
        return c;
    }

    public static long stringToLongCompleteDate(String date, String time){
        Calendar cDate = stringToCalendarDate(date);
        Calendar cTime = stringToCalendarTime(time);
        Log.d(TAG, "stringToLongCompleteDate: cdate="+cDate.getTime());
        cDate.set(Calendar.HOUR_OF_DAY,cTime.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG, "stringToLongCompleteDate: cdate="+cDate.getTime());
        cDate.set(Calendar.MINUTE,cTime.get(Calendar.MINUTE));
        Log.d(TAG, "stringToLongCompleteDate: cDate="+cDate.getTime());
        Log.d(TAG, "stringToLongCompleteDate: getTimeMil="+DateUtils.longToStringCompleteInformationDate(cDate.getTimeInMillis()));
        return cDate.getTimeInMillis();
    }

    public static long stringToLongAllDayDate(String date){
        Calendar cDate = stringToCalendarDate(date);
        return cDate.getTimeInMillis();
    }
    public static String calendarToStringDate (Calendar c){
        return formatDate(c.getTime());
    }

    public static String calendarToStringTime(Calendar c){
        return formatTime(c.getTime());
    }

    public static String calendarToStringLastDay(Calendar c){
        return formatLastDay(c.getTime());
    }

    public static String calendarToStringCompleteInformationDate(Calendar c){
        return formatCompleteInformationDate(c.getTime());
    }

    public static String longtoStringDayMonth(long date){
        Calendar c =Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatDayMonthText.format(c.getTime());
    }
    public static Calendar longToCalendarCompleteDate(long date){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c;
    }


    public static String longToStringTimeDate(long date){
        Calendar c =Calendar.getInstance();
        c.setTimeInMillis(date);
        return formatTimeText.format(c.getTime());
    }

    public static String longToStringCompleteInformationDate(long date){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        Log.d("MADONNA PUTTANA TROIA", "longToStringCompleteInformationDate:"+c.getTime());
        Calendar d = Calendar.getInstance();
        d.set(Calendar.YEAR,c.get(Calendar.YEAR));
        d.set(Calendar.MONTH,c.get(Calendar.MONTH));
        d.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH));
        d.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE,c.get(Calendar.MINUTE));
        return calendarToStringCompleteInformationDate(d);
    }
}