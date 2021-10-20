package com.smartschool.scanit.utils;

import com.smartschool.scanit.shared.Config;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DateTimeFormatUtils {

    public static String getFormattedLocalDateTime(LocalDateTime localDateTime){
        String pattern = localDateTime.format(DateTimeFormatter.ofPattern(Config.getInstance().getDateTimeFormat().getActualDateFormat()));
//        String pattern = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"));
        return pattern;
    }
}
