package com.app.hasanah.Utils.DateAndTime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class CustomDatePickerDialog extends DatePickerDialog {

    public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
       // setLocale(context);
    }

    public CustomDatePickerDialog(Context context, int theme, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, theme, listener, year, month, dayOfMonth);
        //setLocale(context);
    }
}

