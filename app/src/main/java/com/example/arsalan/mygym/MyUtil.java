package com.example.arsalan.mygym;

import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class MyUtil {
    private static final String TAG = "MyUtil";

    /**
     * get string value of time
     *
     * @param remainedItemTime
     * @return
     */
    public static String getStringFormatOfTime(long remainedItemTime) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(remainedItemTime),
                TimeUnit.MILLISECONDS.toMinutes(remainedItemTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remainedItemTime)),
                TimeUnit.MILLISECONDS.toSeconds(remainedItemTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainedItemTime)));
    }

    /**
     * convert time in milisecond to persian date string
     * @param date
     * @return
     */
    public static String getStringFormatOfDate(long date) {

        PersianDate pdate = new PersianDate(date * 1000L);
        PersianDateFormat pdformater1 = new PersianDateFormat("H:i:s y/m/d");
        String out = pdformater1.format(pdate);

        Log.d(TAG, "getStringFormatOfDate: s:" + out+  "date:"+date);
        return out;
    }

    /**
     * convert dp value to pixel
     *
     * @param dpValue value in dp
     * @param r       resources
     * @return value in pixel
     */
    public static float dpToPixel(int dpValue, Resources r) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, r.getDisplayMetrics());
    }
}
