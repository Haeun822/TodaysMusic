package kookmin.todaysmusic.Utils;

import java.net.URL;

import kookmin.todaysmusic.LoadingActivity;

public class Server {
    static String serverUrl = "http://52.11.70.122:8080/";

    public static boolean checkDuplicateID(String ID){
        //TODO Duplicate check process

        return false;
    }

    public static boolean register(String ID, String PW){
        //TODO Register process

        return true;
    }

    public static boolean login(String ID, String PW){
        //TODO Login process

        return true;
    }

    public static void dataLoading(final LoadingActivity loadingActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {


                loadingActivity.goMainActivity();
            }
        }).run();
    }

    public static String serverConn(String servletUrl, String request){
        URL url = new URL(serverUrl + servletUrl);
        String response;

        return response;
    }
}
