package com.example.moviesapp.util;

import android.content.Context;

import com.example.moviesapp.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormatUtils {

    public static String formatNumbers(int number) {
        DecimalFormat decimalFormat = new DecimalFormat(Constant.NUMBER_PATTERN);
        return decimalFormat.format(number);
    }

    public static String formatCurrency(int number) {
        DecimalFormat decimalFormat = new DecimalFormat(Constant.CURRENCY_PATTERN);
        return decimalFormat.format(number);
    }

    public static String formatTime(Context context, int runtime) {
        long hours = TimeUnit.MINUTES.toHours(runtime);
        long minutes = runtime - TimeUnit.HOURS.toMinutes(hours);
        if (minutes == 0) {
            return String.format(Locale.getDefault(), context.getString(R.string.runtime_format_hours), hours);
        } else if (hours == 0) {
            return String.format(Locale.getDefault(), context.getString(R.string.runtime_format_minutes), minutes);
        } else {
            return String.format(Locale.getDefault(), context.getString(R.string.runtime_format), hours, minutes);
        }
    }

    public static String formatDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_PATTERN_INPUT, Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return Constant.NOT_AVAILABLE;
        } else {
            SimpleDateFormat formattedDate = new SimpleDateFormat(Constant.DATE_PATTERN_OUTPUT, Locale.getDefault());
            return formattedDate.format(date);
        }
    }

}
