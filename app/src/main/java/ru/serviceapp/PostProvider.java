package ru.serviceapp;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sergey-rush on 14.04.2018.
 */

public class PostProvider {

    private String TARGET_URL = "https://jsonplaceholder.typicode.com/posts";
    private int responseCode = 0;
    WebContext webContext = WebContext.getInstance();

    public int getPost(int postId) {
        HttpURLConnection connection = null;
        URL url;
        try {

            url = new URL(String.format("%s/%s", TARGET_URL, postId));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String output = webContext.deserializeToString(connection);
                webContext.setPost(webContext.parseToPost(output));
            } else {
                return responseCode;
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return responseCode;
    }
}
