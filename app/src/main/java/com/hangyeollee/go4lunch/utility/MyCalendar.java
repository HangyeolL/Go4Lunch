package com.hangyeollee.go4lunch.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCalendar {

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
}
