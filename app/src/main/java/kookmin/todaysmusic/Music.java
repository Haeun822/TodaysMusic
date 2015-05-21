package kookmin.todaysmusic;

public class Music {
    public static int SERVER_REC = 0;
    public static int USER_REC = 1;

    public String ID = "";
    public String Title = "";
    public String Artist = "";
    public String Album = "";
    public String URL = "";

    public Music(String id){
        ID = id;
    }
}
