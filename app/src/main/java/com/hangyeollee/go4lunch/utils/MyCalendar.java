package com.hangyeollee.go4lunch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCalendar {

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
}
