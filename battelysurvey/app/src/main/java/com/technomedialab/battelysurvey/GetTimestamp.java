package com.technomedialab.battelysurvey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimestamp {

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS,");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

}
