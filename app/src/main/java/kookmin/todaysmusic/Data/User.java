package kookmin.todaysmusic.Data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Pair;

import java.util.ArrayList;

public class User {
    public static String ID;

    public static ArrayList<Music> staredList = new ArrayList<Music>();
    public static ArrayList<Music> recommendList = new ArrayList<Music>();
    public static ArrayList<Music> timeLine = new ArrayList<Music>();
    public static ArrayList<String> followed = new ArrayList<String>();

    public static ArrayList<Pair> getMusicInDevice(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Artists.ARTIST},
                null, null, "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        ArrayList<Pair> list = new ArrayList<Pair>();

        while(cursor.moveToNext()){
            String title = cursor.getString(0);
            String artist = cursor.getString(1);
            list.add(new Pair(title, artist));
        }
        return list;
    }
}
