package kookmin.todaysmusic.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kookmin.todaysmusic.LoadingActivity;

public class Server {
    static String serverUrl = "http://52.11.70.122:8080/TDM/";

    public static boolean checkDuplicateID(String ID){
        try {
            JSONObject json = new JSONObject();
            json.put("ID", ID);

            String response = serverConn("UserCheck", "JSON=" + json);
            json = new JSONObject(response);
            response = json.getString("Check");

            if(response.equals("Can't Find"))
                return false;

            return true;
        } catch(Exception e){ }

        return true;
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
        try {
            URL url = new URL(serverUrl + servletUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream output = conn.getOutputStream();
            output.write(request.getBytes("UTF-8"));
            output.flush();
            output.close();

            InputStreamReader inputReader = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(inputReader);

            String response = reader.readLine();
            return response;
        } catch (Exception e) { }

        return "";
    }
}
