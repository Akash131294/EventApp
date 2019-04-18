package com.eventapp.Utilities;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeUtils {
    public static int getDpToPx(int i, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (i * scale + 0.5f);
    }

    public static String getDateFromTimestamp(long time) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();

        return sdf1.format(date)+" "+sdf2.format(date);
    }
}
