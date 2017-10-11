package com.example.mohsen.myaccountingapp;

import com.alirezaafkar.sundatepicker.components.DateItem;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mohsen on 2017-10-11.
 */

public class DatePersian extends DateItem {
    String getDate() {
        Calendar calendar = getCalendar();
        return String.format(Locale.US,
                "%d/%d/%d",
                getYear(), getMonth(), getDay(),
                calendar.get(Calendar.YEAR),
                +calendar.get(Calendar.MONTH) + 1,
                +calendar.get(Calendar.DAY_OF_MONTH));
    }
}
