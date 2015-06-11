package kookmin.todaysmusic.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class Time {

    public static String getCurrentTime(){
        long time = new Date().getTime();
        Timestamp ts = new Timestamp(time);

        return ts.toString();
    }
}
