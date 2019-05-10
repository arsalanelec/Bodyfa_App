package com.example.arsalan.mygym;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.concurrent.TimeUnit;

public class MyUtil {
    /**
     * get string value of time
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
     * convert dp value to pixel
     * @param dpValue value in dp
     * @param r resources
     * @return value in pixel
     */
    public static float dpToPixel(int dpValue, Resources r) {
            return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpValue, r.getDisplayMetrics());
    }
}
