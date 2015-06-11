package kookmin.todaysmusic.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.StringTokenizer;

import kookmin.todaysmusic.Utils.Server;

public class Music {
    public static int SERVER_REC = 0;
    public static int USER_REC = 1;
    public static int MY_MUSIC = 2;
    public static int SEARCH = 3;

    public static Bitmap blank_thumb;

    public String UserID = "";
    public String MusicID = "";
    public String Title = "";
    public String Artist = "";
    public String URL = "";
    public int Star = -1;
    public String Time = "N";
    public String Feel = "N";
    public String SharedTime = "";
    public boolean isShared = false;

    public Bitmap thumb;

    public Music(String id){
        MusicID = id;
    }

    public void getThumb(){
        if(URL != null){
            String imageID;
            String[] token = URL.split("v=");
            imageID = token[1];

            String imageURL = "http://img.youtube.com/vi/" + imageID + "/default.jpg";
            InputStream input = Server.getStreamFromImage(imageURL);

            if(input != null)
                thumb = BitmapFactory.decodeStream(input);
            else
                thumb = blank_thumb;
        }
    }
}
