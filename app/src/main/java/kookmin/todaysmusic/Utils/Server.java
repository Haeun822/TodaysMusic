package kookmin.todaysmusic.Utils;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kookmin.todaysmusic.LoadingActivity;
import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.Data.User;

public class Server {
    static String serverUrl = "http://52.11.70.122:8080/TDM/";

    public static void register(final String ID, final String PW, final LoadingActivity loadingActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("ID", ID);
                    json.put("PW", PW);

                    String response = serverConn("UserCheck", "JSON=" + json);
                    json = new JSONObject(response);
                    response = json.getString("Check");

                    if(response.equals("Can't Find")){
                        json = new JSONObject();
                        json.put("ID", ID);
                        json.put("PW", PW);

                        response = serverConn("RegisterUser", "JSON=" + json);
                        json = new JSONObject(response);
                        response = json.getString("Check");
                        loadingActivity.userRegisterResult(response);
                    }
                    else
                        loadingActivity.userRegisterResult(response);
                } catch (Exception e) {
                    loadingActivity.userRegisterResult("Error");
                }
            }
        }).start();
    }

    public static void login(final String ID, final String PW, final LoadingActivity loadingActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("ID", ID);
                    json.put("PW", PW);

                    String response = serverConn("UserCheck", "JSON=" + json);
                    json = new JSONObject(response);
                    response = json.getString("Check");

                    loadingActivity.userLoginResult(response);
                } catch (Exception e) { loadingActivity.userLoginResult("Error"); }
            }
        }).start();
    }

    public static void dataInit(final LoadingActivity loadingActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Register MusicList
                    JSONObject json = new JSONObject();
                    JSONArray array = new JSONArray();
                    ArrayList<Pair> list = User.getMusicInDevice(loadingActivity);
                    for(int i=0; i<list.size(); i++){
                        JSONObject jsonTemp = new JSONObject();
                        jsonTemp.put("Title", list.get(0));
                        jsonTemp.put("Artist", list.get(1));
                        array.put(jsonTemp);
                    }
                    json.put("Musics", array);
                    json.put("ID", User.ID);
                    json.put("Type", "Register");
                    serverConn("MusicList", "JSON=" + json);

                    //GET Recommend
                    json = new JSONObject();
                    json.put("ID", User.ID);
                    json.put("Time", "N");
                    json.put("Feel", "N");

                    String response = serverConn("Recommend", "JSON=" + json);
                    json = new JSONObject(response);

                    array = json.getJSONArray("List");
                    for(int i=0; i<array.length(); i++){
                        JSONObject jsonTemp = array.getJSONObject(i);
                        Music music = new Music(jsonTemp.getString("MusicID"));
                        music.Title = jsonTemp.getString("Title");
                        music.Artist = jsonTemp.getString("Artist");
                        music.URL = jsonTemp.getString("URL");
                        music.Star = jsonTemp.getInt("Star");
                        music.Time = jsonTemp.getString("Time");
                        music.Feel = jsonTemp.getString("Feel");

                        User.recommendList.add(music);
                    }
                } catch (Exception e) { }
            }
        }).start();
        loadingActivity.goMainActivity();
    }

    public static void dataLoading(final LoadingActivity loadingActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getMusicList();
                    getFollowed();
                    getTimeLine();
                    getRecommend();

                    loadingActivity.goMainActivity();
                } catch(Exception e){ }
            }
        }).start();
    }

    public static void getMusicList(){
        try {
            JSONObject json = new JSONObject();
            json.put("ID", User.ID);
            json.put("Type", "View");

            String response = serverConn("MusicList", "JSON=" + json);
            User.staredList.clear();
            if (!response.isEmpty()) {
                json = new JSONObject(response);

                JSONArray array = json.getJSONArray("List");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonTemp = array.getJSONObject(i);
                    Music music = new Music(jsonTemp.getString("MusicID"));
                    music.UserID = User.ID;
                    music.Title = jsonTemp.getString("Title");
                    music.Artist = jsonTemp.getString("Artist");
                    music.URL = jsonTemp.getString("URL");
                    music.Star = jsonTemp.getInt("Star");
                    music.Time = jsonTemp.getString("Time");
                    music.Feel = jsonTemp.getString("Feel");
                    music.SharedTime = jsonTemp.getString("SharedTime");
                    music.isShared = jsonTemp.getBoolean("IsShared");

                    music.getThumb();

                    User.staredList.add(music);
                }
            }
        } catch (Exception e) { }
    }

    public static void getFollowed(){
        try{
            JSONObject json = new JSONObject();
            json.put("Follower", User.ID);
            json.put("Type", "Get");

            String response = serverConn("Follow", "JSON=" + json);
            User.followed.clear();
            if(!response.isEmpty()) {
                json = new JSONObject(response);

                JSONArray array = json.getJSONArray("List");
                for (int i = 0; i < array.length(); i++) {
                    User.followed.add((String) array.get(i));
                }
            }
        } catch (Exception e) { }
    }

    public static void getTimeLine(){
        try{
            JSONObject json = new JSONObject();
            json.put("ID", User.ID);
            json.put("start", 0);
            json.put("count", 20);

            String response = serverConn("TimeLine", "JSON=" + json);
            User.timeLine.clear();
            if(!response.isEmpty()) {
                json = new JSONObject(response);

                JSONArray array = json.getJSONArray("List");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonTemp = array.getJSONObject(i);
                    Music music = new Music(jsonTemp.getString("MusicID"));
                    music.UserID = jsonTemp.getString("UserID");
                    music.Title = jsonTemp.getString("Title");
                    music.Artist = jsonTemp.getString("Artist");
                    music.URL = jsonTemp.getString("URL");
                    music.Star = jsonTemp.getInt("Star");
                    music.Time = jsonTemp.getString("Time");
                    music.Feel = jsonTemp.getString("Feel");
                    music.SharedTime = jsonTemp.getString("SharedTime");
                    music.isShared = true;

                    music.getThumb();

                    User.timeLine.add(music);
                }
            }
        } catch (Exception e) { }
    }

    public static void getRecommend(){
        try{
            JSONObject json = new JSONObject();
            json.put("ID", User.ID);
            json.put("Time", "N");
            json.put("Feel", "N");

            String response = serverConn("Recommend", "JSON=" + json);
            User.recommendList.clear();
            if(!response.isEmpty()) {
                json = new JSONObject(response);

                JSONArray array = json.getJSONArray("List");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonTemp = array.getJSONObject(i);
                    Music music = new Music(jsonTemp.getString("track_id"));
                    music.Title = jsonTemp.getString("title");
                    music.Artist = jsonTemp.getString("artist");
                    music.URL = jsonTemp.getString("url");
                    if(jsonTemp.has("Star"))
                        music.Star = jsonTemp.getInt("Star");
                    if(jsonTemp.has("Time"))
                        music.Time = jsonTemp.getString("Time");
                    if(jsonTemp.has("Feel"))
                        music.Feel = jsonTemp.getString("Feel");

                    music.getThumb();

                    User.recommendList.add(music);
                }
            }
        } catch (Exception e) { }
    }

    public static void registerMusicList(Music music){
        ArrayList<Music> m = new ArrayList<Music>();
        m.add(music);
        registerMusicList(m);
    }

    public static void registerMusicList(final ArrayList<Music> musics){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < musics.size(); i++) {
                        JSONObject jsonTemp = new JSONObject();
                        Music m = musics.get(i);
                        jsonTemp.put("ID", m.MusicID);
                        jsonTemp.put("Time", m.Time);
                        jsonTemp.put("Feel", m.Feel);
                        jsonTemp.put("Star", m.Star);
                        jsonTemp.put("IsShared", m.isShared);
                        array.put(jsonTemp);
                    }
                    json.put("Musics", array);
                    json.put("ID", User.ID);
                    json.put("Type", "Register");
                    serverConn("MusicList", "JSON=" + json);
                } catch (Exception e) { }
            }
        }).start();
    }

    public static void deleteMusicList(final Music music){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("ID", User.ID);
                    json.put("MusicID", music.MusicID);
                    json.put("Type", "Delete");
                    serverConn("MusicList", "JSON=" + json);
                } catch (Exception e) { }
            }
        }).start();
    }

    public static String serverConn(String servletUrl, String request){
        try {
            URL url = new URL(serverUrl + servletUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");

            OutputStream output = conn.getOutputStream();
            output.write(request.getBytes("UTF-8"));
            output.flush();
            output.close();

            InputStream input = conn.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputReader);

            String response = reader.readLine();
            return response;
        } catch (Exception e) {
            e.getMessage();
        }

        return "";
    }

    public static InputStream getStreamFromImage(String imageURL){
        try{
            URL url = new URL(imageURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            return conn.getInputStream();
        } catch (Exception e){
            e.getMessage();
        }

        return null;
    }

    public static void searchMusic(ArrayList<Music> list, String title, String artist) {
        list.clear();
        try {
            JSONObject json = new JSONObject();
            json.put("Title", title);
            json.put("Artist", artist);

            String response = serverConn("Search", "JSON=" + json);
            if(!response.isEmpty()) {
                json = new JSONObject(response);
                JSONArray array = json.getJSONArray("tracks");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject temp = array.getJSONObject(i);
                    Music m = new Music(temp.getString("track_id"));
                    m.Title = temp.getString("title");
                    m.Artist = temp.getString("artist");
                    m.URL = temp.getString("url");

                    m.getThumb();

                    list.add(m);
                }
            }

        } catch (Exception e) { }
    }
}
