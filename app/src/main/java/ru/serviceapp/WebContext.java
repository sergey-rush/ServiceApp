package ru.serviceapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import ru.customservice.Post;

/**
 * Created by sergey-rush on 14.04.2018.
 */
public class WebContext {

    private static WebContext current = new WebContext();
    public static WebContext getInstance(){
        return current;
    }
    private Post post;


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    protected Post parseToPost(String input) {
        Post post = new Post();
        try {
            JSONObject resultData = new JSONObject(input);
            post.setId(resultData.getInt("id"));
            post.setUserId(resultData.getInt("userId"));
            post.setTitle(resultData.getString("title"));
            post.setBody(resultData.getString("body"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return post;
    }

    protected void serialisePost(HttpURLConnection connection, String postData) throws IOException {
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();
        os.close();
    }

    protected String deserializeToString(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();

        String output = buffer.toString();
        return output;
    }
}
